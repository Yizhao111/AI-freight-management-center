package vip.penint.cloud.server.busi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Hxx
 * @Description:
 * @since 2023-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("busi_orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @TableField("order_sn")
    private String orderSn;

    @TableField("total_price")
    private BigDecimal totalPrice;

    @TableField("product_description")
    private String productDescription;

    @TableField("notes")
    private String notes;

    @TableField("delivery_date")
    private LocalDate deliveryDate;

    @TableField("order_type")
    private String orderType;

    @TableField("delivery_address")
    private String deliveryAddress;

    @TableField("recipient_name")
    private String recipientName;

    @TableField("phone_number")
    private String phoneNumber;

    @TableField("postal_code")
    private String postalCode;

    @TableField("status")
    private String status;

    @TableField("lat")
    private BigDecimal lat;

    @TableField("lng")
    private BigDecimal lng;


    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("custom_user_id")
    private Integer customUserId;

    @TableField("car_id")
    private Integer carId;

    @TableField("car_num")
    private String carNum;

    @TableField(exist = false)
    private List<OrdersGoods> goodsList;

}
