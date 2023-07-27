package vip.penint.cloud.server.system.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.stereotype.Service;
import vip.penint.cloud.common.core.entity.constant.StringConstant;
import vip.penint.cloud.common.core.entity.system.SysMenu;
import vip.penint.cloud.common.core.entity.system.SysRole;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.common.redis.service.RedisService;
import vip.penint.cloud.server.system.feign.IRemoteAuthService;
import vip.penint.cloud.server.system.service.ISysMenuService;
import vip.penint.cloud.server.system.service.ISysRoleService;
import vip.penint.cloud.server.system.service.ISysUserService;
import vip.penint.cloud.server.system.utils.constant.RedisConstant;

import java.util.List;
import java.util.Set;

@Service
public class CacheManager {

    private RedisService redisService;

    @Autowired(required = false)
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }


    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private IRemoteAuthService remoteAuthService;

    @Autowired
    private RedisConnectionFactory connectionFactory;


    /**
     * 测试 Redis是否连接成功
     */
    public void testConnect() {
        redisService.get("test");
    }

    /**
     * 从缓存中获取用户
     *
     * @param userId 用户Id
     * @return User
     */
    public SysUser getUser(Long userId) throws PenintException {
        if (redisService.hasKey(RedisConstant.USER_USERINFO_CACHE_PREFIX + userId)) {
            String userString = String.valueOf(redisService.get(RedisConstant.USER_USERINFO_CACHE_PREFIX + userId));
            if (StringUtils.isBlank(userString)) {
                throw new PenintException("");
            } else {
                try {
                    return this.mapper.readValue(userString, SysUser.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从缓存中获取用户角色
     *
     * @param userId 用户Id
     * @return 角色集
     */
    public List<SysRole> getRoles(Long userId) throws Exception {
        if (redisService.hasKey(RedisConstant.USER_ROLE_CACHE_PREFIX + userId)) {
            String roleListString = String.valueOf(redisService.get(RedisConstant.USER_ROLE_CACHE_PREFIX + userId));
            if (StringUtils.isBlank(roleListString)) {
                throw new Exception();
            } else {
                JavaType type = mapper.getTypeFactory().constructParametricType(List.class, SysRole.class);
                return this.mapper.readValue(roleListString, type);
            }
        }
        return null;
    }


    /**
     * 从缓存中获取用户权限
     *
     * @param userId 用户Id
     * @return 权限集
     */
    public List<SysMenu> getPermissions(Long userId) throws Exception {
        if (redisService.hasKey(RedisConstant.USER_PERMISSION_CACHE_PREFIX + userId)) {
            String permissionListString = String.valueOf(redisService.get(RedisConstant.USER_PERMISSION_CACHE_PREFIX + userId));
            if (StringUtils.isBlank(permissionListString)) {
                throw new Exception();
            } else {
                JavaType type = mapper.getTypeFactory().constructParametricType(List.class, SysMenu.class);
                return this.mapper.readValue(permissionListString, type);
            }
        }
        return null;
    }

    /**
     * 缓存用户信息，只有当用户信息是查询出来的，完整的，才应该调用这个方法
     * 否则需要调用下面这个重载方法
     *
     * @param userId 用户Id
     */
    public void saveUser(Long userId) {
        deleteUserInfo(userId);
        try {
            redisService.set(RedisConstant.USER_USERINFO_CACHE_PREFIX + userId, mapper.writeValueAsString(this.userService.getInfoById(userId)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存用户信息
     *
     * @param userId 用户Id
     */
    public void saveUserInfo(Long userId) throws PenintException {
        deleteUserInfo(userId);
        SysUser user = this.userService.getInfoById(userId);
        this.deleteUserInfo(userId);
        try {
            redisService.set(RedisConstant.USER_USERINFO_CACHE_PREFIX + userId, mapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存用户角色信息
     *
     * @param userId 用户Id
     */
    public void saveRoles(Long userId) {
        deleteRoles(userId);
        List<SysRole> roleList = this.roleService.findUserRoleByUserId(userId);
        if (!roleList.isEmpty()) {
            this.deleteRoles(userId);
            try {
                redisService.set(RedisConstant.USER_ROLE_CACHE_PREFIX + userId, mapper.writeValueAsString(roleList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 缓存用户权限信息
     *
     * @param userId 用户Id
     */
    public void savePermissions(Long userId) {
        deletePermissions(userId);
        List<SysMenu> permissionList = this.menuService.findUserPermissions(userId);
        deletePermissions(userId);
        if (!permissionList.isEmpty()) {
            this.deletePermissions(userId);
            redisService.set(RedisConstant.USER_PERMISSION_CACHE_PREFIX + userId, JSONArray.toJSONString(permissionList));

        }
    }


    /**
     * 删除用户缓存信息
     *
     * @param userId 用户Id
     */
    public void deleteUserInfo(Long userId) {
        redisService.del(RedisConstant.USER_USERINFO_CACHE_PREFIX + userId);
    }

    /**
     * 删除用户角色信息
     *
     * @param userId 用户Id
     */
    public void deleteRoles(Long userId) {
        redisService.del(RedisConstant.USER_ROLE_CACHE_PREFIX + userId);
    }

    /**
     * 删除用户权限信息
     *
     * @param userId 用户Id
     */
    public void deletePermissions(Long userId) {
        redisService.del(RedisConstant.USER_PERMISSION_CACHE_PREFIX + userId);
    }

    /**
     * 删除用户个性化配置
     *
     * @param userId 用户 ID
     */
    public void deleteUserConfigs(Long userId) {
        redisService.del(RedisConstant.USER_CONFIG_CACHE_PREFIX + userId);
    }

    public void loadUserByUser(Integer roleId) {
        Set<String> keys = redisService.keys("auth:*");
        System.out.println(keys);
        if (roleId != null) {
            for (String key : keys) {
                OAuth2Authentication authentication = getByKey(key);
                System.out.println(authentication);
                Object principal = authentication.getPrincipal();
                JSONObject data = JSONObject.parseObject(JSON.toJSONString(principal));
                String roleIds = data.getString("roleId");
                String[] roleArr = roleIds.split(",");
                key = StringUtils.replace(key, "auth:", StringConstant.EMPTY);
                for (String roleIdStr : roleArr) {
                    if (roleId.equals(Integer.valueOf(roleIdStr))) {
                        remoteAuthService.loadUserByUserToken(key);
                    }
                }
//            System.out.println(principal);
                // 通过查询redis key 信息判断是否属于这个角色
            }
        }

//        SysUser user = userService.getById(userId);
    }

    public OAuth2Authentication getByKey(String key) {
        RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
        byte[] serializedKey = serializationStrategy.serialize(key);
        RedisConnection conn = connectionFactory.getConnection();
        byte[] bytes;
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
        return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }
}
