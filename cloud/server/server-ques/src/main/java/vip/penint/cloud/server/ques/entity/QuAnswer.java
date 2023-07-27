package vip.penint.cloud.server.ques.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Penint
 * @Description:候选答案
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_qu_answer")
public class QuAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("qu_id")
    private String quId;

    @TableField("content")
    private String content;


    @TableField(exist = false)
    private Integer checkNum;
    @TableField(exist = false)
    private Integer all;
}
