package vip.penint.cloud.server.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import vip.penint.cloud.common.core.entity.system.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author penint
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> findUserPermissions(@Param("userId") Long userId);

    List<SysMenu> findUserMenus(@Param("userId") Long userId);

    List<String> findUserPermissionsByUserId(@Param("userId") Long userId);

    List<SysMenu> selectMenuTreeByUserId(@Param("userId") Long userId);

    List<SysMenu> selectMenuListByUserId(@Param("menu") SysMenu menu, @Param("userId") Long userId);

    List<Integer> selectMenuListByRoleId(@Param("roleId") Integer roleId);

    List<SysMenu> selectMenuList(@Param("menu") SysMenu menu);

}
