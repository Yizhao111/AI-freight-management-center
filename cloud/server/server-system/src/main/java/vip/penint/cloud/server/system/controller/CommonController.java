package vip.penint.cloud.server.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.system.SysRole;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.utils.StringUtils;
import vip.penint.cloud.server.system.manager.UserManager;
import vip.penint.cloud.server.system.service.ISysMenuService;
import vip.penint.cloud.server.system.service.ISysRoleService;
import vip.penint.cloud.server.system.service.ISysUserRoleService;
import vip.penint.cloud.server.system.service.ISysUserService;

import java.util.Date;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysUserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ISysUserRoleService userRoleService;


    @GetMapping("/selectRole")
    public AjaxResult selectRole() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(SysRole::getRoleId, 1);
        return AjaxResult.success(sysRoleService.list(wrapper));
    }


    @PostMapping("/register")
    public AjaxResult register(@RequestBody SysUser sysUser) {
        // 查询用户名是否存在数据库中
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername, sysUser.getUsername());
        SysUser user = userService.getOne(lambdaQueryWrapper);
        if (user == null) {
            sysUser.setCreateTime(new Date());
            // 设置默认密码1234qwer
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            userService.save(sysUser);
            // 插入返回ID
            Long userId = sysUser.getUserId();
            // 新增角色用户关联
            if (StringUtils.isNotEmpty(sysUser.getRoleId())) {
                userRoleService.insertByRoleList(userId, new Integer[]{Integer.valueOf(sysUser.getRoleId())});
            }
            // 存入缓存
            userManager.loadOneUserRedisCache(userId);
            return AjaxResult.success("用户:" + sysUser.getUsername() + "注册成功");
        } else {
            return AjaxResult.error("用户注册失败，用户名：" + sysUser.getUsername() + "已存在");
        }
    }
}
