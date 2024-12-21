package pers.jhshop.es.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.lang.reflect.ParameterizedType;
import java.util.List;
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
    public void insertOrUpdate(T t){}

    /**
     * 批量数据插入或更新
     */
    public void batchInsertOrUpdate(List<T> dataList){}

    /**
     * 获取对象的字段信息
     */
    // TODO 暂时用不到

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
        NativeSearchQuery searchQuery = getNativeSearchQuery(queryReq);
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
     * 根据条件查询
     */
    private NativeSearchQuery getNativeSearchQuery(QueryReq queryReq) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        // 组装查询条件
        BoolQueryBuilder boolQueryBuilder = getBoolQueryBuilder(queryReq);
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

        // 设置分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of((int) (queryReq.getCurrent() - 1), (int) (queryReq.getSize())));

        // 设置排序
        withSort(queryReq, nativeSearchQueryBuilder);

        // 设置查询字段
        withSourceFilter(queryReq, nativeSearchQueryBuilder);

        NativeSearchQuery build = nativeSearchQueryBuilder.build();
        // 设置计算总行数
        build.setTrackTotalHits(true);

        return build;
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

}
