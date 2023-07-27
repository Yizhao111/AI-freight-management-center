package vip.penint.cloud.common.security.starter.feign;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 配置注册
 *
 **/
@Configuration
public class FeignAutoConfiguration
{
    @Bean
    public RequestInterceptor requestInterceptor()
    {
        return new FeignRequestInterceptor();
    }
}
