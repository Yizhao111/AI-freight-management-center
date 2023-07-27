package vip.penint.cloud.server.busi.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
    
/**
 * @Description:财务表
 * @author Hxx
 * @since 2023-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("busi_finance")
public class Finance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("type")
    private Integer type;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("create_time")
    private LocalDateTime createTime;




}
