package vip.penint.cloud.server.ques.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Penint
 * @Description:问卷回答表
 * @since 2022-11-09
 */
@Data
@Accessors
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_exam_answer", autoResultMap = true)
public class ExamAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("exam_id")
    private String examId;

    @TableField("user_id")
    private Integer userId;

    @TableField(value = "`data`", typeHandler = JacksonTypeHandler.class)
    private JSONObject data;

    @TableField("create_time")
    private LocalDateTime createTime;


}
