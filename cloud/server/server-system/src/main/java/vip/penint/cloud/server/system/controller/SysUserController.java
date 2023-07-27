package vip.penint.cloud.server.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.utils.PenintUtil;
import vip.penint.cloud.common.core.utils.StringUtils;
import vip.penint.cloud.server.system.manager.UserManager;
import vip.penint.cloud.server.system.service.ISysUserRoleService;
import vip.penint.cloud.server.system.service.ISysUserService;
import vip.penint.cloud.server.system.utils.UploadFileUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author penint
 */
@RestController
@RequestMapping("/adminApi/sysUser")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ISysUserRoleService userRoleService;

    private final PasswordEncoder passwordEncoder;

    public SysUserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:user:view')")
    public AjaxResult list(SysUser user) {
        return AjaxResult.success(userService.queryFuzz(user));
    }

    @GetMapping("/userByRole")
    public AjaxResult userByRole(Integer roleId) {
        return AjaxResult.success(userService.selectUserByRole(roleId));
    }

    @GetMapping("/listAll")
    public AjaxResult listAll(SysUser user) {
        return AjaxResult.success(userService.queryFuzz(user));
    }


    /**
     * 修改账户状态，禁用或者取消
     *
     * @param user
     * @param request
     * @return
     */
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user, HttpServletRequest request) {
        Long userId = PenintUtil.getCurrentUser().getUserId();
        if (userManager.isAdmin(userId)) {

            userService.updateById(user);

            // 清除token
//            redisService.del(RedisConstant.PENINT_TOKEN_CACHE + userService.getById(user.getUserId()).getUsername());
            return AjaxResult.success();
        } else {
            return AjaxResult.error(201, "无权限，admin 用户有此权限");
        }
//        return AjaxResult.success();

    }

    /**
     * 根据ID查询用户详情
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public AjaxResult getUserInfo(@PathVariable Long userId) {
        SysUser info = userService.getInfoById(userId);
        String roleId = info.getRoleId();
        String postId = info.getPostId();
        if (StringUtils.isNotEmpty(roleId)) {
            String[] str = roleId.split(",");
            Integer[] aftIdArray = (Integer[]) ConvertUtils.convert(str, Integer.class);
            info.setRoleIds(aftIdArray);
        }
        if (StringUtils.isNotEmpty(postId)) {
            String[] str = postId.split(",");
            Integer[] aftIdArray = (Integer[]) ConvertUtils.convert(str, Integer.class);
            info.setPostIds(aftIdArray);
        }
        return AjaxResult.success(info);
    }

    /**
     * 根据ID查询用户详情
     *
     * @param userId
     * @return
     */
    @GetMapping("/info")
    public PenintResponse info(@RequestParam Long userId) {
        SysUser info = userService.getInfoById(userId);
        return new PenintResponse().data(info);
    }


    /**
     * 新增用户
     *
     * @param sysUser
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public AjaxResult add(@RequestBody SysUser sysUser) {
        // 查询用户名是否存在数据库中
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername, sysUser.getUsername());
        SysUser user = userService.getOne(lambdaQueryWrapper);
        if (user == null) {
            sysUser.setCreateTime(new Date());
            // 设置默认密码1234qwer
            sysUser.setPassword(passwordEncoder.encode(SysUser.DEFAULT_PASSWORD));
            userService.save(sysUser);
            // 插入返回ID
            Long userId = sysUser.getUserId();
            // 新增角色用户关联
            if (sysUser.getRoleIds().length > 0) {
                userRoleService.insertByRoleList(userId, sysUser.getRoleIds());
            }
            // 存入缓存
            userManager.loadOneUserRedisCache(userId);
            return AjaxResult.success("用户:" + sysUser.getUsername() + "新增成功，默认密码为：" + SysUser.DEFAULT_PASSWORD);
        } else {
            return AjaxResult.error("用户新增失败，用户名：" + sysUser.getUsername() + "已存在");
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        boolean flag = false;
        for (Long userId : userIds) {
            if (!userManager.isAdmin(userId)) {
                userService.removeById(userId);
                // 删除中间表中的关联关系
                userManager.delMiddleTb(userId);
                // 删除缓存
                userManager.deleteUserRedisCache(userId);
            } else {
                flag = true;
            }
        }
        if (flag) {
            return AjaxResult.error(201, "admin用户不能删除");
        } else {
            return AjaxResult.success();
        }
    }

    /**
     * 修改用户
     *
     * @param sysUser
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public AjaxResult edit(@RequestBody SysUser sysUser) {
        userService.updateUser(sysUser);
        // 重新刷新缓存
        userManager.loadOneUserRedisCache(sysUser.getUserId());
        return AjaxResult.success();
    }

    /**
     * 重置密码
     *
     * @param sysUser
     * @return
     */
    @PutMapping("/resetPwd")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public AjaxResult reSetPwd(@RequestBody SysUser sysUser) {
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        userService.updateById(sysUser);
        return AjaxResult.success();
    }


    /**
     * 个人中心配置
     */
    @GetMapping("/profile")
    public AjaxResult profile() {

        Long userId = PenintUtil.getCurrentUser().getUserId();
        return AjaxResult.success(userManager.getUser(userId));
    }

    /**
     * 个人中心配置
     */
    @PutMapping("/profile")
    public AjaxResult updateProfile(@RequestBody SysUser sysUser) {
        userService.updateById(sysUser);
        // 刷新缓存
        userManager.saveUser(sysUser.getUserId());
        return AjaxResult.success();
    }

    @PutMapping("/profile/updatePwd")
    public AjaxResult profileUpdatePwd(QueryRequest queryRequest, HttpServletRequest request) {
        String username = PenintUtil.getCurrentUsername();
        Long userId = PenintUtil.getCurrentUser().getUserId();
        SysUser user = userService.getById(userId);
        if (passwordEncoder.matches(queryRequest.getOldPassword(), user.getPassword())) {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(userId);
            sysUser.setPassword(passwordEncoder.encode(queryRequest.getNewPassword()));
            userService.updateById(sysUser);
            return AjaxResult.success();
        } else {
            return AjaxResult.error(201, "原密码不正确");
        }
    }

    @DeleteMapping("/deleteUserRedisCache")
    public PenintResponse deleteUserRedisCache() {
        Long userId = PenintUtil.getCurrentUser().getUserId();
        userManager.deleteUserRedisCache(userId);
        return new PenintResponse().message("OK");
    }

    @PostMapping("/profile/avatar")
    public AjaxResult avatarUpload(@RequestParam("avatarfile") MultipartFile file, HttpServletRequest request) {
        Long userId = PenintUtil.getCurrentUser().getUserId();
        JSONObject result = UploadFileUtils.upload(file);
        if (result.getInteger("status") == 200) {
            if (result.getBoolean("isImage")) {
                SysUser user = new SysUser();
                user.setAvatar(result.getString("requestUrl"));
                user.setUserId(userId);
                userService.updateById(user);
                // 缓存
                userManager.saveUser(userId);
                return AjaxResult.success(result.getString("requestUrl"));
            } else {
                return AjaxResult.error("不是图片文件");
            }
        } else {
            return AjaxResult.error("文件上传失败,请联系管理员");
        }

    }
}
