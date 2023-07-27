package vip.penint.cloud.server.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import vip.penint.cloud.common.core.entity.system.SysUser;

import java.util.List;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author penint
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> queryFuzz(@Param("user") SysUser user, IPage<SysUser> userIPage);

    SysUser getInfoById(@Param("userId") Long userId);

    SysUser getRoleById(@Param("userId") Long userId);

    List<SysUser> queryList(@Param("user") SysUser user);

    List<SysUser> selUser(@Param("userIds") String userIds);

    IPage<SysUser> pageUserList(@Param("user") SysUser user, IPage<SysUser> page);

    SysUser findByName(@Param("username") String username);

    List<SysUser> selectUserByRole(@Param("roleId") Integer roleId);
}
