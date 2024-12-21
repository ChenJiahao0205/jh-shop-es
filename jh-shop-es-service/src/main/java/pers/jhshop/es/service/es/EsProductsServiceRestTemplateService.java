package pers.jhshop.es.service.es;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import pers.jhshop.common.utils.JhShopDateUtil;
import pers.jhshop.es.core.AbstractElasticSearchRestTemplateService;
import pers.jhshop.es.model.es.EsProductsEntity;
import pers.jhshop.es.model.req.EsProductsQueryReq;

import java.util.List;
import java.util.Objects;

/**
 * 商品ES服务实现类
 * @author ChenJiahao(五条)
 * @date 2024/12/21 14:29:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EsProductsServiceRestTemplateService extends AbstractElasticSearchRestTemplateService<EsProductsEntity, EsProductsQueryReq> {

    @Override
    protected void withSourceFilter(EsProductsQueryReq esProductsQueryReq, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        List<String> queryFieldList = esProductsQueryReq.getQueryFields();
        if (CollectionUtils.isNotEmpty(queryFieldList)){
            String[] includes = queryFieldList.toArray(new String[0]);
            nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(includes, null));
        }
    }

    @Override
    protected BoolQueryBuilder getBoolQueryBuilder(EsProductsQueryReq esProductsQueryReq) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 获取并判断 id
        Long id = esProductsQueryReq.getId();
        if (Objects.nonNull(id)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }

        // 获取并判断 name
        String name = esProductsQueryReq.getName();
        if (StringUtils.isNotBlank(name)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("name", name));
        }

        // 获取并判断 productDescription
        String productDescription = esProductsQueryReq.getProductDescription();
        if (StringUtils.isNotBlank(productDescription)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("productDescription", productDescription));
        }

        // 获取并判断 brand
        String brand = esProductsQueryReq.getBrand();
        if (StringUtils.isNotBlank(brand)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand", brand));
        }

        // 获取并判断 categoryId
        Integer categoryId = esProductsQueryReq.getCategoryId();
        if (Objects.nonNull(categoryId)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryId", categoryId));
        }

        // 获取并判断 price
        String price = esProductsQueryReq.getPrice();
        if (StringUtils.isNotBlank(price)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("price", price));
        }

        // 获取并判断 stock
        Integer stock = esProductsQueryReq.getStock();
        if (Objects.nonNull(stock)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("stock", stock));
        }

        // 获取并判断 status
        Boolean status = esProductsQueryReq.getStatus();
        if (Objects.nonNull(status)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("status", status));
        }

        // 获取并判断 createdAt
        String createdAt = JhShopDateUtil.getStrTime(esProductsQueryReq.getCreatedAt());
        if (StringUtils.isNotBlank(createdAt)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("createdAt", createdAt));
        }

        // 获取并判断 updatedAt
        String updatedAt = JhShopDateUtil.getStrTime(esProductsQueryReq.getUpdatedAt());
        if (StringUtils.isNotBlank(updatedAt)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("updatedAt", updatedAt));
        }

        // 获取并判断 imageUrl
        String imageUrl = esProductsQueryReq.getImageUrl();
        if (StringUtils.isNotBlank(imageUrl)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("imageUrl", imageUrl));
        }

        // 获取并判断 description
        String description = esProductsQueryReq.getDescription();
        if (StringUtils.isNotBlank(description)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("description", description));
        }

        // 获取并判断 validFlag
        Boolean validFlag = esProductsQueryReq.getValidFlag();
        if (Objects.nonNull(validFlag)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("validFlag", validFlag));
        }

        // 获取并判断 createTime
        String createTime = JhShopDateUtil.getStrTime(esProductsQueryReq.getCreateTime());
        if (StringUtils.isNotBlank(createTime)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("createTime", createTime));
        }

        // 获取并判断 updateTime
        String updateTime = JhShopDateUtil.getStrTime(esProductsQueryReq.getUpdateTime());
        if (StringUtils.isNotBlank(updateTime)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("updateTime", updateTime));
        }

        return boolQueryBuilder;
    }

    @Override
    protected void withSort(EsProductsQueryReq esProductsQueryReq, NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        if (StringUtils.isNotBlank(esProductsQueryReq.getSortField())) {
            FieldSortBuilder fieldSortBuilder = new FieldSortBuilder(esProductsQueryReq.getSortField());
            SortOrder sortOrder = esProductsQueryReq.getSortOrder();
            fieldSortBuilder.order(Objects.isNull(sortOrder) ? SortOrder.ASC : sortOrder);
            nativeSearchQueryBuilder.withSort(fieldSortBuilder);
        }
    }
}
