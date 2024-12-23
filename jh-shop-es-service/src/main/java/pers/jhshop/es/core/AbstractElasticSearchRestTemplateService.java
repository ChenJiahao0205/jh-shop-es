package pers.jhshop.es.core;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.BulkOptions;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ES RestTemplateService模板
 * @author ChenJiahao(五条)
 * @date 2024/12/21 14:35:03
 */
@Slf4j
public abstract class AbstractElasticSearchRestTemplateService<T, QueryReq extends Page> {

    private Class<T> clazz = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 单数据插入或更新
     */
    public void insertOrUpdate(T t){
        FieldIdInfo fieldIdInfo = getFieldIdInfo(t);
        if (Objects.isNull(fieldIdInfo)) {
            throw new RuntimeException("未找到id字段" );
        }

        String idValue = fieldIdInfo.getIdValue();

        Map<String, Object> sourceMap = JSON.parseObject(JSON.toJSONString(t), Map.class);
        Map<String, Object> filteredMap = sourceMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        UpdateQuery.Builder builder = UpdateQuery.builder(idValue);

        builder.withDocument(Document.from(filteredMap));
        builder.withUpsert(Document.from(filteredMap));
        builder.withDocAsUpsert(true);
        builder.withRetryOnConflict(5);
        UpdateQuery updateQuery = builder.build();

        ArrayList<UpdateQuery> updateQueries = new ArrayList<>();
        updateQueries.add(updateQuery);

        BulkOptions bulkOptions = BulkOptions.builder().withRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).build();
        elasticsearchRestTemplate.bulkUpdate(updateQueries, bulkOptions, elasticsearchRestTemplate.getIndexCoordinatesFor(clazz));
    }

    /**
     * 批量数据插入或更新
     */
    public void batchInsertOrUpdate(List<T> dataList){
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }

        T t = dataList.get(0);
        FieldIdInfo fieldIdInfo = getFieldIdInfo(t);
        if (Objects.isNull(fieldIdInfo)) {
            throw new RuntimeException("未找到id字段" );
        }

        String idFieldName = fieldIdInfo.getIdFieldName();

        List<Map<String, Object>> filteredMapList = dataList.stream().map(item -> {
            Map<String, Object> sourceMap = JSON.parseObject(JSON.toJSONString(item), Map.class);
            return sourceMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }).collect(Collectors.toList());

        ArrayList<UpdateQuery> updateQueries = new ArrayList<>(filteredMapList.size());
        for(Map<String, Object> filteredMap : filteredMapList) {
            Object id = filteredMap.get(idFieldName);

            // id不能为空
            if (Objects.isNull(id)) {
                throw new RuntimeException("batchInsertOrUpdate--id不能为空" );
            }

            UpdateQuery.Builder builder = UpdateQuery.builder(id.toString());

            builder.withDocument(Document.from(filteredMap));
            builder.withUpsert(Document.from(filteredMap));
            builder.withDocAsUpsert(true);
            UpdateQuery updateQuery = builder.build();

            updateQueries.add(updateQuery);
        }

        BulkOptions bulkOptions = BulkOptions.builder().withRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).build();
        elasticsearchRestTemplate.bulkUpdate(updateQueries, bulkOptions, elasticsearchRestTemplate.getIndexCoordinatesFor(clazz));
    }

    /**
     * 主键查询
     */
    public T getById(Long id){
        if (Objects.isNull(id)){
            return null;
        }

        return elasticsearchRestTemplate.get(String.valueOf(id), clazz);
    }

    /**
     * 根据条件查询一条数据
     */
    public T getOne(QueryReq queryReq){
        queryReq.setCurrent(1);
        queryReq.setSize(1);
        NativeSearchQuery searchQuery = getNativeSearchQuery(queryReq);
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(searchQuery, clazz);
        SearchHit<T> searchHit = searchHits.getSearchHit(0);
        return searchHit.getContent();
    }

    /**
     * 根据条件查询列表
     */
    public List<T> list(QueryReq queryReq){
        queryReq.setCurrent(queryReq.getCurrent());
        queryReq.setSize(queryReq.getSize());
        NativeSearchQuery searchQuery = getNativeSearchQueryNoSetTotal(queryReq);
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(searchQuery, clazz);

        List<SearchHit<T>> searchHitList = searchHits.getSearchHits();
        if (CollectionUtils.isEmpty(searchHitList)){
            return null;
        }

        return searchHitList.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * 根据条件分页查询
     */
    public Page<T> page(QueryReq queryReq){
        NativeSearchQuery searchQuery = getNativeSearchQuery(queryReq);
        SearchHits<T> searchHits = elasticsearchRestTemplate.search(searchQuery, clazz);
        long totalHits = searchHits.getTotalHits();
        long totalPage = totalHits / queryReq.getSize();

        Page<T> page = new Page<>(queryReq.getCurrent(), queryReq.getSize());
        page.setTotal(totalHits);
        page.setPages(totalPage);

        List<SearchHit<T>> searchHitList = searchHits.getSearchHits();
        if (CollectionUtils.isEmpty(searchHitList)){
            return page;
        }

        List<T> records = searchHitList.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(records)){
            return page;
        }

        page.setRecords(records);
        return page;
    }

    /**
     * 根据条件构造列表查询 不设置total
     */
    private NativeSearchQuery getNativeSearchQueryNoSetTotal(QueryReq queryReq) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        // 1.组装查询条件
        BoolQueryBuilder bqb = getBoolQueryBuilder(queryReq);
        nativeSearchQueryBuilder.withQuery(bqb);
        // 2.设置分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of((int) (queryReq.getCurrent() - 1), (int) (queryReq.getSize())));
        // 3.设置排序
        withSort(queryReq, nativeSearchQueryBuilder);
        // 4.设置查询的字段
        withSourceFilter(queryReq, nativeSearchQueryBuilder);
        return nativeSearchQueryBuilder.build();
    }

    /**
     * 根据条件查询
     */
    private NativeSearchQuery getNativeSearchQuery(QueryReq queryReq) {
        NativeSearchQuery build = getNativeSearchQueryNoSetTotal(queryReq);
        // 设置计算总行数
        build.setTrackTotalHits(true);
        return build;
    }

    /**
     * 获取对象的id字段信息
     */
    private FieldIdInfo getFieldIdInfo(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields) {
            if (field.isAnnotationPresent(org.springframework.data.annotation.Id.class)) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    log.error("获取id值失败", e);
                    continue;
                }

                FieldIdInfo fieldIdInfo = new FieldIdInfo();
                fieldIdInfo.setIdFieldName(field.getName());
                fieldIdInfo.setIdValue(value != null ? value.toString() : null);

                return fieldIdInfo;
            }
        }

        return null;
    }

    /**
     * 指定查询字段
     */
    protected void withSourceFilter(QueryReq queryReq, NativeSearchQueryBuilder nativeSearchQueryBuilder){}

    /**
     * 查询条件
     */
    protected BoolQueryBuilder getBoolQueryBuilder(QueryReq queryReq){
        return QueryBuilders.boolQuery();
    }

    /**
     * 排序
     */
    protected void withSort(QueryReq queryReq, NativeSearchQueryBuilder nativeSearchQueryBuilder){}

    /**
     * id字段实体
     */
    @Data
    private static class FieldIdInfo {
        /**
         * id字段名
         */
        private String idFieldName;

        /**
         * id字段值
         */
        private String idValue;
    }
}
