package vip.penint.cloud.server.busi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description:订单商品表
 * @author Hxx
 * @since 2023-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("busi_orders_goods")
public class OrdersGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("goods_id")
    private Integer goodsId;

    @TableField("goods_name")
    private String goodsName;

    @TableField("number")
    private Integer number;

    @TableField("price")
    private BigDecimal price;

    @TableField("order_id")
    private Integer orderId;




}
