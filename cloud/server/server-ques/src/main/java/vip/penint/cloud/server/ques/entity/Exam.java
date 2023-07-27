package vip.penint.cloud.server.ques.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.penint.cloud.common.core.entity.system.SysUser;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Penint
 * @Description:
 * @since 2022-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_exam")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("open_type")
    private Integer openType;

    @TableField("num")
    private Integer num;

    @TableField("time_limit")
    private Boolean timeLimit = false;

    @TableField(value = "start_time", updateStrategy = FieldStrategy.IGNORED)
    private LocalDate startTime;

    @TableField(value = "stop_time", updateStrategy = FieldStrategy.IGNORED)
    private LocalDate stopTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("start_user")
    private Long startUser;

    @TableField("com_id")
    private String comId;

    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;

    @TableField("status")
    private String status;

    @TableField(value = "user_id_str", updateStrategy = FieldStrategy.IGNORED)
    private String userIdStr;

    @TableField(value = "theme")
    private Integer theme;

    @TableField(exist = false)
    private List<Qu> selectList;

    @TableField(exist = false)
    private List<SysUser> selectUserList;

    @TableField(exist = false)
    private List<String> dateValues;

    @TableField(exist = false)
    private List<Long> uids;
}
