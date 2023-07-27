package vip.penint.cloud.server.busi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
    
/**
 * @Description:车辆管理
 * @author Hxx
 * @since 2023-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("busi_car")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("car_num")
    private String carNum;

    @TableField("user_id")
    private Integer userId;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;




}
