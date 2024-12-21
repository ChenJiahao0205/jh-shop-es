package pers.jhshop.es.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ES商品查询VO
 * @author ChenJiahao(五条)
 * @date 2024/12/21 16:46:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "EsProductsQueryVo", description = "ES商品查询VO")
public class EsProductsQueryVo {

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
}
