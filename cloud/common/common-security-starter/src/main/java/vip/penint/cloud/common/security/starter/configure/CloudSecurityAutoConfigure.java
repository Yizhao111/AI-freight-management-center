package vip.penint.cloud.common.security.starter.configure;

import vip.penint.cloud.common.core.entity.constant.PenintConstant;
import vip.penint.cloud.common.core.utils.PenintUtil;
import vip.penint.cloud.common.security.starter.handler.PenintAccessDeniedHandler;
import vip.penint.cloud.common.security.starter.handler.AuthExceptionEntryPoint;
import vip.penint.cloud.common.security.starter.properties.CloudSecurityProperties;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.util.Base64Utils;

/**
 * 
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(CloudSecurityProperties.class)
@ConditionalOnProperty(value = "penint.cloud.security.enable", havingValue = "true", matchIfMissing = true)
public class CloudSecurityAutoConfigure extends GlobalMethodSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public PenintAccessDeniedHandler accessDeniedHandler() {
        return new PenintAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public AuthExceptionEntryPoint authenticationEntryPoint() {
        return new AuthExceptionEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CloudSecurityInteceptorConfigure cloudSecurityInteceptorConfigure() {
        return new CloudSecurityInteceptorConfigure();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(DefaultTokenServices.class)
    public UserInfoTokenServices userInfoTokenServices(ResourceServerProperties properties) {
        return new UserInfoTokenServices(properties.getUserInfoUri(), properties.getClientId());
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            String gatewayToken = new String(Base64Utils.encode(PenintConstant.GATEWAY_TOKEN_VALUE.getBytes()));
            requestTemplate.header(PenintConstant.GATEWAY_TOKEN_HEADER, gatewayToken);
            String authorizationToken = PenintUtil.getCurrentTokenValue();
            if (StringUtils.isNotBlank(authorizationToken)) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, PenintConstant.OAUTH2_TOKEN_TYPE + authorizationToken);
            }
        };
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
