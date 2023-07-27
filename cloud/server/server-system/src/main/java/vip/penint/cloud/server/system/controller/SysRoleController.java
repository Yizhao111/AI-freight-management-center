package vip.penint.cloud.server.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.entity.system.SysRole;
import vip.penint.cloud.common.core.entity.system.SysRoleMenu;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.system.manager.UserManager;
import vip.penint.cloud.server.system.service.ISysRoleMenuService;
import vip.penint.cloud.server.system.service.ISysRoleService;
import vip.penint.cloud.server.system.service.ISysUserRoleService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author penint
 */
@RestController
@RequestMapping("/adminApi/sysRole")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private UserManager userManager;


    /**
     * 查看角色
     *
     * @param queryRequest
     * @param sysRole
     * @return
     */
    @PreAuthorize("hasAuthority('system:role:view')")
    @GetMapping("/list")
    public AjaxResult list(QueryRequest queryRequest, SysRole sysRole) {

        return AjaxResult.success(roleService.queryList(queryRequest, sysRole));
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add')")
    public AjaxResult addRole(@Validated @RequestBody SysRole sysRole) {
        sysRole.setCreateTime(new Date());
        roleService.save(sysRole);
        Integer[] menuIds = sysRole.getMenuIds();
        setRoleMenus(sysRole, menuIds);

        return AjaxResult.success();
    }


    /**
     * 添加角色数据时，往rolemenu中间表中新增关系
     *
     * @param role
     * @param menuIds
     */
    private void setRoleMenus(SysRole role, Integer[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setMenuId(menuId);
            rm.setRoleId(role.getRoleId());
            sysRoleMenuService.save(rm);
        });
    }

    /**
     * 通过roleId删除角色
     * 已使用的角色禁止删除
     *
     * @param roleId
     * @return
     */
    @DeleteMapping("/{roleIds}")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public AjaxResult deleteByRoleId(@PathVariable("roleIds") Integer roleId) {

        // 判断角色是否被使用
        if (roleService.countUserRoleByRoleId(roleId) > 0) {
            return AjaxResult.error("角色已经被用户绑定，请解除绑定再删除");
        } else {
            // 删除角色
            roleService.removeById(roleId);
            // 删除roleMenu表中关联的数据
            LambdaQueryWrapper<SysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
            sysRoleMenuService.remove(lambdaQueryWrapper);
            return AjaxResult.success();
        }
    }

    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/{roleId}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public AjaxResult getInfo(@PathVariable Integer roleId) {
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 修改角色
     */
    @PutMapping
    @PreAuthorize("hasAuthority('system:role:edit')")
    public AjaxResult updateRole(@RequestBody SysRole role) throws PenintException {
        try {
            // 查询角色关联了哪些用户
            List<Long> userId = userRoleService.findUserIdsByRoleId(role.getRoleId());
            // 修改角色，并重新赋予菜单权限
            roleService.updateRole(role);
            // 重新刷新缓存
            userManager.loadBatchUserRedisCache(userId, role.getRoleId());
            return AjaxResult.success();
        } catch (Exception e) {
            throw new PenintException("修改角色失败");
        }
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("hasAuthority('system:role:edit')")
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.authDataScope(role);
        return AjaxResult.success();
    }
}
