package vip.penint.cloud.common.core.entity.constant;

/**
 * 系统常量类
 *
 * 
 */
public interface PenintConstant {

    /**
     * 排序规则：降序
     */
    String ORDER_DESC = "descending";
    /**
     * 排序规则：升序
     */
    String ORDER_ASC = "ascending";

    /**
     * Gateway请求头TOKEN名称（不要有空格）
     */
    String GATEWAY_TOKEN_HEADER = "GatewayToken";
    /**
     * Gateway请求头TOKEN值
     */
    String GATEWAY_TOKEN_VALUE = "penint:gateway:yzkj:123456";

    /**
     * 允许下载的文件类型，根据需求自己添加（小写）
     */
    String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    /**
     * 验证码 key前缀
     */
    String CODE_PREFIX = "penint.captcha.";

    /**
     * 异步线程池名称
     */
    String ASYNC_POOL = "PenintAsyncThreadPool";

    /**
     * OAUTH2 令牌类型 https://oauth.net/2/bearer-tokens/
     */
    String OAUTH2_TOKEN_TYPE = "bearer";
    /**
     * Java默认临时目录
     */
    String JAVA_TEMP_DIR = "java.io.tmpdir";
    /**
     * utf-8
     */
    String UTF8 = "utf-8";
    /**
     * 注册用户角色ID
     */
    Integer REGISTER_ROLE_ID = 2;

    String LOCALHOST = "localhost";
    String LOCALHOST_IP = "127.0.0.1";

}
