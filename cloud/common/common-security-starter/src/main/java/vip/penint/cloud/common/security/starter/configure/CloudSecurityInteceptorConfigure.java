package vip.penint.cloud.common.security.starter.configure;

import vip.penint.cloud.common.security.starter.interceptor.ServerProtectInterceptor;
import vip.penint.cloud.common.security.starter.properties.CloudSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 */
public class CloudSecurityInteceptorConfigure implements WebMvcConfigurer {

    private CloudSecurityProperties properties;

    @Autowired
    public void setProperties(CloudSecurityProperties properties) {
        this.properties = properties;
    }

    @Bean
    public HandlerInterceptor serverProtectInterceptor() {
        ServerProtectInterceptor serverProtectInterceptor = new ServerProtectInterceptor();
        serverProtectInterceptor.setProperties(properties);
        return serverProtectInterceptor;
    }

    @Override
    @SuppressWarnings("all")
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverProtectInterceptor());
    }
}
