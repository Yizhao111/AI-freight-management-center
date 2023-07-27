package vip.penint.cloud.server.busi.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description:类目表
 * @author Hxx
 * @since 2023-07-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("busi_shop_category")
public class ShopCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("`desc`")
    private String desc;

    @TableField("pid")
    private Integer pid;

    @TableField("icon_url")
    private String iconUrl;

    @TableField("pic_url")
    private String picUrl;

    @TableField("level")
    private String level;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("sort")
    private Integer sort;


    @TableField(exist = false)
    private List<ShopCategory> children;



}
