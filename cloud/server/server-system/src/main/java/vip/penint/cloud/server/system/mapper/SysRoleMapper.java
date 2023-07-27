package vip.penint.cloud.server.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.entity.system.SysRole;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author penint
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> findUserRole(@Param("username") String username);

    List<SysRole> findUserRoleByUserId(@Param("userId") Long userId);

    int countUserRoleByRoleId(Integer roleId);

    SysRole selectRoleById(@Param("roleId") Integer roleId);

    IPage<SysRole> queryList(@Param("rolePage") IPage<SysRole> rolePage, @Param("queryRequest") QueryRequest queryRequest, @Param("sysRole") SysRole sysRole);

}
