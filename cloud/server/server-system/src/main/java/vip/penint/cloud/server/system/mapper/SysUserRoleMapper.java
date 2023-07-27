package vip.penint.cloud.server.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import vip.penint.cloud.common.core.entity.system.SysUserRole;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author penint
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    void insertByRoleList(@Param("userId") Long userId, @Param("roleIds") Integer[] roleIds);

    List<Long> findUserIdsByMenuId(@Param("menuId") Integer menuId);

    void deleteUserRoleByUserId(@Param("userId") Long userId);

    void batchUserRole(@Param("list") List<SysUserRole> list);
}
