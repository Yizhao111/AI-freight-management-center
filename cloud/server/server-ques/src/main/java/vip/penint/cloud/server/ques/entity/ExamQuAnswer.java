package vip.penint.cloud.server.ques.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Penint
 * @Description:问卷题目备选答案
 * @since 2022-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_exam_qu_answer")
public class ExamQuAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("exam_id")
    private String examId;

    @TableField("answer_id")
    private String answerId;

    @TableField("qu_id")
    private String quId;

    @TableField("sort")
    private Integer sort;

    @TableField("abc")
    private String abc;

    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;


}
