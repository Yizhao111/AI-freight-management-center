package vip.penint.cloud.auth.manager;

import vip.penint.cloud.auth.mapper.MenuMapper;
import vip.penint.cloud.auth.mapper.UserMapper;
import vip.penint.cloud.auth.mapper.UserRoleMapper;
import vip.penint.cloud.common.core.entity.constant.PenintConstant;
import vip.penint.cloud.common.core.entity.constant.StringConstant;
import vip.penint.cloud.common.core.entity.system.SysMenu;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.entity.system.SysUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户业务逻辑
 *
 *
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserManager {

    private final UserMapper userMapper;
    private final MenuMapper menuMapper;
    private final UserRoleMapper userRoleMapper;

    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户
     */
    public SysUser findByName(String username) {
        SysUser user = userMapper.findByName(username);
//        if (user != null) {
//            List<UserDataPermission> permissions = userMapper.findUserDataPermissions(user.getUserId());
//            String deptIds = permissions.stream().map(p -> String.valueOf(p.getDeptId())).collect(Collectors.joining(StringConstant.COMMA));
//            user.setDeptIds(deptIds);
//        }
        return user;
    }

    /**
     * 通过用户名查询用户权限串
     *
     * @param username 用户名
     * @return 权限
     */
    public String findUserPermissions(String username) {
        List<SysMenu> userPermissions = menuMapper.findUserPermissions(username);
        return userPermissions.stream().map(SysMenu::getPerms).collect(Collectors.joining(StringConstant.COMMA));
    }

    /**
     * 注册用户
     *
     * @param username username
     * @param password password
     * @return SystemUser SystemUser
     */
    @Transactional(rollbackFor = Exception.class)
    public SysUser registUser(String username, String password) {
        SysUser systemUser = new SysUser();
        systemUser.setUsername(username);
        systemUser.setPassword(password);
        systemUser.setCreateTime(new Date());
        systemUser.setStatus(SysUser.STATUS_VALID);
        systemUser.setSsex(SysUser.SEX_UNKNOW);
        systemUser.setAvatar(SysUser.DEFAULT_AVATAR);
        systemUser.setDescription("注册用户");
        this.userMapper.insert(systemUser);

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(systemUser.getUserId());
        // 注册用户角色 ID
        userRole.setRoleId(PenintConstant.REGISTER_ROLE_ID);
        this.userRoleMapper.insert(userRole);
        return systemUser;
    }
}
