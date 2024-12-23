package pers.jhshop.es.model.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    @Id
    @Field(type = FieldType.Long)
    private Long id;

    /**
     * 商品名称
     */
    @Field(type = FieldType.Text)
    private String name;

    /**
     * 商品描述
     */
    @Field(type = FieldType.Keyword)
    private String productDescription;

    /**
     * 商品品牌
     */
    @Field(type = FieldType.Keyword)
    private String brand;

    /**
     * 商品分类 ID
     */
    @Field(type = FieldType.Integer)
    private Integer categoryId;

    /**
     * 商品价格
     */
    @Field(type = FieldType.Keyword)
    private String price;

    /**
     * 商品库存
     */
    @Field(type = FieldType.Integer)
    private Integer stock;

    /**
     * 商品状态
     */
    @Field(type = FieldType.Boolean)
    private Boolean status;

    /**
     * 商品创建时间
     */
    @Field(type = FieldType.Keyword)
    private String createdAt;

    /**
     * 商品最后更新时间
     */
    @Field(type = FieldType.Keyword)
    private String updatedAt;

    /**
     * 商品主图 URL
     */
    @Field(type = FieldType.Keyword)
    private String imageUrl;

    /**
     * 商品的附加描述
     */
    @Field(type = FieldType.Keyword)
    private String description;

    /**
     * 生效标志
     */
    @Field(type = FieldType.Boolean)
    private Boolean validFlag;

    /**
     * 记录的创建时间
     */
    @Field(type = FieldType.Keyword)
    private String createTime;

    /**
     * 记录的更新时间
     */
    @Field(type = FieldType.Keyword)
    private String updateTime;
}
