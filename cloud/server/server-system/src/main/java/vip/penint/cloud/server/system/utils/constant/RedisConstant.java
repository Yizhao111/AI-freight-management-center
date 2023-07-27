package vip.penint.cloud.server.system.utils.constant;

/**
 * 一些常用的常量
 */
public interface RedisConstant {


    // user用户信息前缀+userId
    String USER_USERINFO_CACHE_PREFIX = "PENINT_CLOUD_USERINFO_CACHE_";
    // user角色缓存前缀+userId
    String USER_ROLE_CACHE_PREFIX = "PENINT_CLOUD_ROLE_CACHE_USER_";
    // user权限缓存前缀+userId
    String USER_PERMISSION_CACHE_PREFIX = "PENINT_CLOUD_PERMISSION_CACHE_";
    // user个性化配置前缀+userId
    String USER_CONFIG_CACHE_PREFIX = "PENINT_CLOUD_CONFIG_CACHE_USER_";

    /**
     * 验证码 redis key
     */
    String PENINT_CAPTCHA_CODE_KEY = "penint_cloud_captcha_codes:";

    /**
     * 验证码有效期（分钟）
     */
    Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 缓存在线用户Zset集合
     */
    String PENINT_ACTIVE_USERS_ZSET_PREFIX = "PENINT_CLOUD_ACTIVE_USERS_ZSET_PREFIX";

}
