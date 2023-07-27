package vip.penint.cloud.server.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.penint.cloud.common.core.entity.system.SysUserRole;
import vip.penint.cloud.server.system.mapper.SysUserRoleMapper;
import vip.penint.cloud.server.system.service.ISysUserRoleService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author penint
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public void insertByRoleList(Long userId, Integer[] roleIds) {
        sysUserRoleMapper.insertByRoleList(userId, roleIds);
    }

    @Override
    public List<Long> findUserIdsByRoleId(Integer roleId) {
        List<SysUserRole> list = baseMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));
        return list.stream().map(userRole -> userRole.getUserId()).collect(Collectors.toList());
    }

    @Override
    public List<Long> findUserIdsByMenuId(Integer menuId) {
        return sysUserRoleMapper.findUserIdsByMenuId(menuId);
    }
}
