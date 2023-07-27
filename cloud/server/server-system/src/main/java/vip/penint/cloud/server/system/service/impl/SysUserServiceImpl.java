package vip.penint.cloud.server.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.entity.system.SysUserRole;
import vip.penint.cloud.common.core.utils.StringUtils;
import vip.penint.cloud.server.system.mapper.SysUserMapper;
import vip.penint.cloud.server.system.mapper.SysUserRoleMapper;
import vip.penint.cloud.server.system.service.ISysUserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author penint
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;


    @Override
    public IPage<SysUser> queryFuzz(SysUser user) {
        IPage<SysUser> userIPage = new Page<>(user.getPageNum(), user.getPageSize(), true);
        return sysUserMapper.queryFuzz(user, userIPage);
    }

    @Override
    public List<SysUser> queryList(SysUser user) {
        return sysUserMapper.queryList(user);
    }

    @Override
    public SysUser getInfoById(Long userId) {
        return sysUserMapper.getInfoById(userId);
    }

    @Override
    public SysUser getRoleById(Long userId) {
        return sysUserMapper.getRoleById(userId);
    }

    @Override
    public void updateUser(SysUser sysUser) {
        // 修改用户基本信息
        sysUserMapper.updateById(sysUser);
        Long userId = sysUser.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(sysUser);
    }


    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        Integer[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Integer roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    @Override
    public List<SysUser> selUser(String userIds) {
        return sysUserMapper.selUser(userIds);
    }


    @Override
    public IPage<SysUser> pageUserList(SysUser user) {
        IPage<SysUser> page = new Page<>(user.getPageNum(), user.getPageSize(), true);
        return sysUserMapper.pageUserList(user, page);
    }

    @Override
    public void updateLoginTime(Long userId, Date now, String ip) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setLastLoginTime(now);
        user.setLastLoginIp(ip);
        sysUserMapper.updateById(user);
    }

    @Override
    public SysUser findByName(String username) {
        return sysUserMapper.findByName(username);

    }

    @Override
    public List<SysUser> selectUserByRole(Integer roleId) {
        return sysUserMapper.selectUserByRole(roleId);
    }
}
