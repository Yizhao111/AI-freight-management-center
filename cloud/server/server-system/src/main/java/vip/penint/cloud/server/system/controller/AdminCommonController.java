package vip.penint.cloud.server.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.CurrentUser;
import vip.penint.cloud.common.core.entity.system.SysMenu;
import vip.penint.cloud.common.core.entity.system.SysRole;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.utils.PenintUtil;
import vip.penint.cloud.server.system.manager.UserManager;
import vip.penint.cloud.server.system.service.ISysMenuService;
import vip.penint.cloud.server.system.service.ISysRoleService;
import vip.penint.cloud.server.system.service.ISysUserService;
import vip.penint.cloud.server.system.utils.AddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/adminApi/common")
@RequiredArgsConstructor
public class AdminCommonController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysUserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserManager userManager;


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/userInfo")
    public AjaxResult userInfo(HttpServletRequest request) {

        Long userId = PenintUtil.getCurrentUser().getUserId();
        // 用户详细信息
//        SysUser user = userService.getById(userId);
        SysUser user = userManager.getUser(userId);

        // 角色集合
//        Set<String> roles = roleService.selectRolePermissionByUserId(userId);
        Set<String> roles = userManager.getUserRoles(userId);
        // 权限集合 getUserPermissions
//        Set<String> permissions = menuService.findUserPermissionsByUserId(userId);
        Set<String> permissions = userManager.getUserPermissions(userId);

        JSONObject data = new JSONObject();

        data.put("user", user);
        data.put("roles", roles);
        data.put("permissions", permissions);
        return AjaxResult.success(data);
    }

    /**
     * 更新用户登录信息及保存登陆日志
     *
     * @param request
     */
    @GetMapping("success")
    public void loginSuccess(HttpServletRequest request) {
        CurrentUser currentUser = PenintUtil.getCurrentUser();
        String ip = PenintUtil.getHttpServletRequestIpAddress();
        String address = AddressUtil.getCityInfo(ip);
        Date now = new Date();
        // update last login time
        userService.updateLoginTime(currentUser.getUserId(), now, ip);
        // save redis info
        userManager.loadOneUserRedisCache(currentUser.getUserId());
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public AjaxResult getRouters(HttpServletRequest request) {

        Long userId = PenintUtil.getCurrentUser().getUserId();
        List<SysMenu> menuList = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menuList));
    }

    /**
     * 获取角色下拉框
     *
     * @return 返回id，name
     */
    @GetMapping("/getRole")
    public AjaxResult getRole() {
        BaseMapper<SysRole> baseMapper = sysRoleService.getBaseMapper();
        return AjaxResult.success(baseMapper);
    }

    /**
     * 添加角色时获取菜单树
     *
     * @param request
     * @param menu
     * @return
     */
    @GetMapping("/getMenuTree")
    public AjaxResult getMenuTree(HttpServletRequest request, SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuListByUserId(menu, PenintUtil.getCurrentUser().getUserId());
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeSelect/{roleId}")
    public AjaxResult roleMenuTreeSelect(@PathVariable("roleId") Integer roleId, HttpServletRequest request) {
        List<SysMenu> menus = menuService.selectMenuList(PenintUtil.getCurrentUser().getUserId());
        JSONObject data = new JSONObject();
        data.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        data.put("menus", menuService.buildMenuTreeSelect(menus));
        return AjaxResult.success(data);
    }


    /**
     * 判断库中用户名 有没有被使用
     *
     * @param username
     * @return
     */
    @GetMapping("/checkUsername/{username}")
    public AjaxResult checkUsername(@PathVariable String username) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername, username);
        if (userService.getOne(lambdaQueryWrapper) != null) {
            return AjaxResult.success(true);
        } else {
            return AjaxResult.success(false);
        }
    }

    /**
     * 判断请求方法的该用户原密码是否正确
     *
     * @param password
     * @return
     */
    @GetMapping("/checkPassword/{password}")
    public AjaxResult checkPassword(@PathVariable String password) {
        Long userId = PenintUtil.getCurrentUser().getUserId();
        SysUser user = userService.getById(userId);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return AjaxResult.success(true);
        } else {
            return AjaxResult.success(false);
        }
    }


    /**
     * 查询用户列表
     * 分页
     */
    @GetMapping("/pageUserList")
    public AjaxResult pageUserList(SysUser user) {
        return AjaxResult.success(userService.pageUserList(user));
    }


    @GetMapping("/selectUserList")
    public AjaxResult selectUserList() {
        return AjaxResult.success(userService.list());
    }

}
