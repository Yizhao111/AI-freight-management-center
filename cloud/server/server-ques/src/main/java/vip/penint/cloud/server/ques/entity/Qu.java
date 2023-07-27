package vip.penint.cloud.server.ques.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Penint
 * @Description:题目表
 * @since 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_qu")
public class Qu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("qu_type")
    private Integer quType;

    @TableField("level")
    private Integer level;

    @TableField("content")
    private String content;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("com_id")
    private String comId;

    @TableField(exist = false)
    private String publishName;
//
//    @TableField(exist = false)
//    private List<String> repoIds;

    @TableField(exist = false)
    private List<QuAnswer> answerList;

    @TableField(exist = false)
    private Integer quCount;

    @TableField(exist = false)
    private JSONObject stat;

}
