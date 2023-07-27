package vip.penint.cloud.auth.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 *
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:auth.properties"})
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    /**
     * 验证码配置
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();
    /**
     * JWT加签密钥
     */
    private String jwtAccessKey;
    /**
     * 是否使用 JWT令牌
     */
    private Boolean enableJwt;

    /**
     * 社交登录所使用的 Client
     */
    private String socialLoginClientId;
}
