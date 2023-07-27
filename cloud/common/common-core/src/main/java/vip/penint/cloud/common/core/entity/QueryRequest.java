package vip.penint.cloud.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

/**
 *
 */
@Data
@ToString
public class QueryRequest<T>  {

    @TableField(exist = false)
    private Integer pageSize = 10;
    @TableField(exist = false)
    private Integer pageNum = 1;
    @TableField(exist = false)
    private String beginTime;
    @TableField(exist = false)
    private String endTime;
    @TableField(exist = false)
    private String oldPassword;
    @TableField(exist = false)
    private String newPassword;
    /**
     * 排序字段
     */
    @TableField(exist = false)
    private String field;
    /**
     * 排序规则，asc升序，desc降序
     */
    @TableField(exist = false)
    private String order;
}
