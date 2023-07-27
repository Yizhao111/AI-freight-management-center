package vip.penint.cloud.server.system.service;

import vip.penint.cloud.common.core.entity.system.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author penint
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    void insertByRoleList(Long userId, Integer[] roleIds);

    List<Long>  findUserIdsByRoleId(Integer roleId);

    List<Long> findUserIdsByMenuId(Integer menuId);
}
