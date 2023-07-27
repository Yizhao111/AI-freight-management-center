package vip.penint.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.penint.cloud.common.core.entity.system.SysMenu;

import java.util.List;

/**
 *
 */
public interface MenuMapper extends BaseMapper<SysMenu> {

    /**
     * 获取用户权限集
     *
     * @param username 用户名
     * @return 权限集合
     */
    List<SysMenu> findUserPermissions(String username);
}