package vip.penint.cloud.common.core.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import vip.penint.cloud.common.core.utils.excel.LocalDateTimeConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity基类
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    @ExcelIgnore
    private String searchValue;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @ExcelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间", converter = LocalDateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
