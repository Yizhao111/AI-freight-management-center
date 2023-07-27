package vip.penint.cloud.server.ques.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Penint
 * @Description:试卷考题
 * @since 2022-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_exam_qu")
public class ExamQu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("exam_id")
    private String examId;

    @TableField("qu_id")
    private String quId;

    @TableField("qu_type")
    private Integer quType;

    @TableField("sort")
    private Integer sort;

    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;


}
