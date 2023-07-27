package vip.penint.cloud.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author penint
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单/按钮id
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    /**
     * 上级菜单id
     */
    private Integer parentId;

    /**
     * 菜单/按钮名称
     */
    private String menuName;

    /**
     * 对应路由path
     */
    private String path;

    /**
     * 对应路由组件component
     */
    private String component;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 图标
     */
    private String icon;

    /**
     * 类型 0菜单 1按钮
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 是否是外链
     */
    private Boolean isLink;

    /**
     * 是否显示
     */
    private Boolean isVisible;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<SysMenu>();


    public List<SysMenu> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }


    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
