package pers.jhshop.es.model.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;
import pers.jhshop.es.model.es.EsProductsEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ES商品查询Req
 * @author ChenJiahao(五条)
 * @date 2024/12/21 14:31:18
 */
@Data
@ApiModel(value = "EsProductsQueryReq", description = "ES商品查询Req")
public class EsProductsQueryReq extends Page<EsProductsEntity> implements Serializable {

    /**
     * 商品唯一标识
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String productDescription;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品分类 ID
     */
    private Integer categoryId;

    /**
     * 商品价格
     */
    private String price;

    /**
     * 商品库存
     */
    private Integer stock;

    /**
     * 商品状态
     */
    private Boolean status;

    /**
     * 商品创建时间
     */
    private Date createdAt;

    /**
     * 商品最后更新时间
     */
    private Date updatedAt;

    /**
     * 商品主图 URL
     */
    private String imageUrl;

    /**
     * 商品的附加描述
     */
    private String description;

    /**
     * 生效标志
     */
    private Boolean validFlag;

    /**
     * 记录的创建时间
     */
    private Date createTime;

    /**
     * 记录的更新时间
     */
    private Date updateTime;

    // =============指定查询字段================
    /**
     * 指定查询字段
     */
    private List<String> queryFields;

    // =================排序===================
    /**
     * 排序字段名 需要传入正确的字段名(包含驼峰) {@link EsProductsEntity}
     */
    private String sortField;

    /**
     * 排序方式
     */
    private SortOrder sortOrder;
}
