package vip.penint.cloud.server.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.system.SysMenu;
import vip.penint.cloud.common.core.utils.PenintUtil;
import vip.penint.cloud.server.system.manager.UserManager;
import vip.penint.cloud.server.system.service.ISysMenuService;
import vip.penint.cloud.server.system.service.ISysUserRoleService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author penint
 */
@RestController
@RequestMapping("/adminApi/sysMenu")
public class SysMenuController {

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private UserManager userManager;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:menu:view')")
    public AjaxResult list(SysMenu menu, HttpServletRequest request) {
//        Integer userId = JWTUtil.getUserId(request);
        List<SysMenu> menus = menuService.selectMenuListByUserId(menu, PenintUtil.getCurrentUser().getUserId());
        return AjaxResult.success(menuService.buildMenuTree(menus));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:add')")
    public AjaxResult add(@RequestBody SysMenu menu) {
        menu.setCreateTime(new Date());
        menuService.save(menu);
        return AjaxResult.success();
    }

    @GetMapping("/{menuId}")
    public AjaxResult getInfo(@PathVariable Integer menuId) {
        return AjaxResult.success(menuService.getById(menuId));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public AjaxResult edit(@RequestBody SysMenu menu) {
        menu.setModifyTime(new Date());
        menuService.updateById(menu);
        // 查找与这些菜单/按钮关联的用户
        List<Long> userIds = userRoleService.findUserIdsByMenuId(menu.getMenuId());
        userManager.loadBatchUserRedisCache(userIds, null);
        return AjaxResult.success();
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    public AjaxResult delete(@PathVariable Integer menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return AjaxResult.error(201, "存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return AjaxResult.error(201, "菜单已分配,不允许删除");
        }

        // 查找与这些菜单/按钮关联的用户
        List<Long> userIds = userRoleService.findUserIdsByMenuId(menuId);
        menuService.removeById(menuId);
        userManager.loadBatchUserRedisCache(userIds, null);

        return AjaxResult.success();
    }
}
