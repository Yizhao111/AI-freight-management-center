package vip.penint.cloud.server.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.common.core.entity.TreeSelect;
import vip.penint.cloud.common.core.entity.router.RouterVo;
import vip.penint.cloud.common.core.entity.system.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author penint
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> findUserPermissions(Long userId);

    List<SysMenu> findUserMenus(Long userId);

    Set<String> findUserPermissionsByUserId(Long userId);


    List<SysMenu> selectMenuTreeByUserId(Long userId);

    List<RouterVo> buildMenus(List<SysMenu> menuList);

    List<SysMenu> selectMenuListByUserId(SysMenu menu, Long userId);

    List<SysMenu> buildMenuTree(List<SysMenu> menus);

    List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    List<SysMenu> selectMenuList(Long userId);

    List<Integer> selectMenuListByRoleId(Integer roleId);

    /**
     * 查询菜单是否有角色使用
     * @param menuId
     * @return
     */
    boolean checkMenuExistRole(Integer menuId);

    /**
     * 是否存在菜单子节点
     */
    boolean hasChildByMenuId(Integer menuId);

}
