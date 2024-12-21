package pers.jhshop.es.model.es;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Es商品索引
 * @author ChenJiahao(五条)
 * @date 2024/12/21 14:11:22
 */
@Data
@Document(indexName = "es_products")
public class EsProductsEntity {

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
    private String createdAt;

    /**
     * 商品最后更新时间
     */
    private String updatedAt;

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
    private String createTime;

    /**
     * 记录的更新时间
     */
    private String updateTime;
}
