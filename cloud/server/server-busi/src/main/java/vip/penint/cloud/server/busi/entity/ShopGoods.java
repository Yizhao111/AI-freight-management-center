package vip.penint.cloud.server.busi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Hxx
 * @Description:商品表
 * @since 2023-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("busi_shop_goods")
public class ShopGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("goods_sn")
    private String goodsSn;

    @TableField("name")
    private String name;

    @TableField("category_id")
    private Integer categoryId;

    @TableField("brief")
    private String brief;

    @TableField("is_on_sale")
    private Boolean isOnSale;


    @TableField("unit")
    private String unit;

    @TableField("sku")
    private Integer sku;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("category_source")
    private String categorySource;

    @TableField("price")
    private BigDecimal price;


}
