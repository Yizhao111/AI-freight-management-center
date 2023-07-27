package vip.penint.cloud.common.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import vip.penint.cloud.common.core.entity.system.SysMenu;
import vip.penint.cloud.common.core.entity.system.SysUser;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 */
public class TreeSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private Integer id;

    private Integer value;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 部门下用户数量
     */
    private List<SysUser> user;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect() {

    }

    public TreeSelect(SysMenu menu) {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect> children) {
        this.children = children;
    }

    public List<SysUser> getUser() {
        return user;
    }

    public void setUser(List<SysUser> user) {
        this.user = user;
    }
}
