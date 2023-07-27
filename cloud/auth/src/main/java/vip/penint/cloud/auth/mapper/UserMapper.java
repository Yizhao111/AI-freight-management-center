package vip.penint.cloud.auth.mapper;

import vip.penint.cloud.common.core.entity.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 *
 */
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * 获取用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser findByName(String username);

    /**
     * 获取用户数据权限
     *
     * @param userId 用户id
     * @return 数据权限
     */
//    List<UserDataPermission> findUserDataPermissions(Long userId);
}
