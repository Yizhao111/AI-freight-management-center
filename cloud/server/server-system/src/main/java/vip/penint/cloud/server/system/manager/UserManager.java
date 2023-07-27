package vip.penint.cloud.server.system.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vip.penint.cloud.common.core.entity.system.SysMenu;
import vip.penint.cloud.common.core.entity.system.SysRole;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.entity.system.SysUserRole;
import vip.penint.cloud.common.core.utils.PenintUtil;
import vip.penint.cloud.server.system.service.ISysMenuService;
import vip.penint.cloud.server.system.service.ISysRoleService;
import vip.penint.cloud.server.system.service.ISysUserRoleService;
import vip.penint.cloud.server.system.service.ISysUserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 封装一些和 User相关的业务操作
 */
@Service
public class UserManager {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    @Lazy
    private ISysRoleService roleService;
    @Autowired
    @Lazy
    private ISysMenuService menuService;
    @Autowired
    @Lazy
    private ISysUserService userService;
    @Autowired
    @Lazy
    private ISysUserRoleService userRoleService;


    /**
     * 通过用户名获取用户基本信息
     *
     * @param username 用户名
     * @return 用户基本信息
     */
    public SysUser getUser(String username) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername, username);
        return this.userService.getOne(lambdaQueryWrapper);
    }

    /**
     * 通过用户Id获取用户基本信息
     *
     * @return 用户基本信息
     */
    public SysUser getUser(Long userId) {
        return PenintUtil.selectCacheByTemplate(() -> this.cacheManager.getUser(userId), () -> this.userService.getInfoById(userId));
    }

    public void saveUser(Long userId) {
        cacheManager.saveUser(userId);
    }


    /**
     * 通过用户Id获取用户角色集合
     *
     * @param userId 用户Id
     * @return 角色集合
     */
    public Set<String> getUserRoles(Long userId) {
        List<SysRole> roleList = PenintUtil.selectCacheByTemplate(() -> this.cacheManager.getRoles(userId), () -> this.roleService.findUserRoleByUserId(userId));
        return roleList.stream().map(SysRole::getRoleName).collect(Collectors.toSet());
    }

    /**
     * TODO 可用
     * 通过用户Id获取用户权限集合
     *
     * @param userId 用户Id
     * @return 权限集合
     */
    public Set<String> getUserPermissions(Long userId) {
        List<SysMenu> permissionList = PenintUtil.selectCacheByTemplate(() -> cacheManager.getPermissions(userId), () -> menuService.findUserPermissions(userId));
        return permissionList.stream().map(SysMenu::getPerms).collect(Collectors.toSet());
    }

    /**
     * 将用户相关信息添加到 Redis缓存中
     *
     * @param userId 用户Id
     */
    @Async
    public void loadOneUserRedisCache(Long userId) {
        // 缓存用户
        cacheManager.saveUser(userId);
        // 缓存用户角色
        cacheManager.saveRoles(userId);
        // 缓存用户权限
        cacheManager.savePermissions(userId);
    }

    /**
     * 将用户角色和权限添加到 Redis缓存中
     *
     * @param userIds userIds
     */
    public void loadBatchUserRedisCache(List<Long> userIds, Integer roleId) {
        for (Long userId : userIds) {
            // 缓存用户角色
            cacheManager.saveUser(userId);
            // 缓存用户权限
            cacheManager.savePermissions(userId);
            // 缓存角色权限
            cacheManager.saveRoles(userId);
            cacheManager.loadUserByUser(roleId);
        }
    }

    /**
     * 通过用户id集合批量删除用户 Redis缓存
     *
     * @param userIds userIds
     */
    public void deleteBatchUserRedisCache(List<Long> userIds) {
        for (Long userId : userIds) {
            cacheManager.deleteUserInfo(userId);
            cacheManager.deleteRoles(userId);
            cacheManager.deletePermissions(userId);
            cacheManager.deleteUserConfigs(userId);
        }
    }

    public void deleteUserRedisCache(Long userId) {
        cacheManager.deleteUserInfo(userId);
        cacheManager.deleteRoles(userId);
        cacheManager.deletePermissions(userId);
        cacheManager.deleteUserConfigs(userId);
    }


    /**
     * 根据UserId 判断是不是超级管理员(admin)
     */
    public boolean isAdmin(Long userId) {
        SysUser user = userService.getById(userId);
        if (user.getUsername().equalsIgnoreCase("admin")) {
            return true;
        } else {
            return false;
        }
    }

    public void delMiddleTb(Long userId) {
        // 删除用户角色表中对应关系
        LambdaQueryWrapper<SysUserRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUserRole::getUserId, userId);
        userRoleService.remove(lambdaQueryWrapper);
    }
}
