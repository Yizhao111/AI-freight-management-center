package vip.penint.cloud.server.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.penint.cloud.common.core.entity.system.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.common.core.entity.QueryRequest;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author penint
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysRole> findUserRole(String username);

    List<SysRole> findUserRoleByUserId(Long userId);

    IPage<SysRole> queryList(QueryRequest queryRequest, SysRole sysRole);

    Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 通过角色ID查询角色使用数量
     * @param roleId
     * @return
     */
    int countUserRoleByRoleId(Integer roleId);

    SysRole selectRoleById(Integer roleId);

    void updateRole(SysRole role);

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    void authDataScope(SysRole role);

    List<SysRole> roleList();
}
