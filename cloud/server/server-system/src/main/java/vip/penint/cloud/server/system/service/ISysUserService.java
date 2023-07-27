package vip.penint.cloud.server.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.common.core.entity.system.SysUser;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author penint
 */
public interface ISysUserService extends IService<SysUser> {

    IPage<SysUser> queryFuzz(SysUser user);

    List<SysUser> queryList(SysUser user);

    SysUser getInfoById(Long userId);

    SysUser getRoleById(Long userId);

    void updateUser(SysUser sysUser);

    List<SysUser> selUser(String userIds);

    IPage<SysUser> pageUserList(SysUser user);

    /**
     * 更新登录时间
     *
     * @param userId
     * @param now
     * @param ip
     */
    void updateLoginTime(Long userId, Date now, String ip);

    SysUser findByName(String username);

    List<SysUser> selectUserByRole(Integer roleId);
}
