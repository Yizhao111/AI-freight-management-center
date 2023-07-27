package vip.penint.cloud.server.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.entity.system.SysRole;
import vip.penint.cloud.common.core.entity.system.SysRoleMenu;
import vip.penint.cloud.server.system.mapper.SysRoleMapper;
import vip.penint.cloud.server.system.service.ISysRoleMenuService;
import vip.penint.cloud.server.system.service.ISysRoleService;
import vip.penint.cloud.server.system.service.ISysUserRoleService;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author penint
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ISysUserRoleService userRoleService;

    @Autowired
    private ISysRoleMenuService roleMenuService;


    @Override
    public List<SysRole> findUserRole(String username) {
        return sysRoleMapper.findUserRole(username);
    }

    @Override
    public List<SysRole> findUserRoleByUserId(Long userId) {
        return sysRoleMapper.findUserRoleByUserId(userId);
    }

    @Override
    public IPage<SysRole> queryList(QueryRequest queryRequest, SysRole sysRole) {
        IPage<SysRole> rolePage = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);

        return sysRoleMapper.queryList(rolePage, queryRequest, sysRole);
    }


    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        List<SysRole> userRoleByUserId = this.findUserRoleByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : userRoleByUserId) {
            if (perm != null) {
                permsSet.addAll(Arrays.asList(perm.getRoleName().trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public int countUserRoleByRoleId(Integer roleId) {
        return sysRoleMapper.countUserRoleByRoleId(roleId);
    }

    @Override
    public SysRole selectRoleById(Integer roleId) {
        return sysRoleMapper.selectRoleById(roleId);
    }

    @Override
    public void updateRole(SysRole role) {
        // 查找这些角色关联了那些用户

        Integer roleId = role.getRoleId();
        role.setModifyTime(new Date());
        sysRoleMapper.updateById(role);

        // 删除角色菜单关联表中的相关role数据
        LambdaQueryWrapper<SysRoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        roleMenuService.remove(lambdaQueryWrapper);


        Integer[] menuIds = role.getMenuIds();
        setRoleMenus(roleId, menuIds);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authDataScope(SysRole role) {
        // 修改角色信息
        sysRoleMapper.updateById(role);
    }

    private void setRoleMenus(Integer roleId, Integer[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setMenuId(Integer.valueOf(menuId));
            rm.setRoleId(roleId);
            roleMenuService.save(rm);
        });
    }


    @Override
    public List<SysRole> roleList() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysRole::getRoleId, SysRole::getRoleName);
        return sysRoleMapper.selectList(wrapper);
    }
}
