package vip.penint.cloud.common.security.starter.configure;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import vip.penint.cloud.common.core.entity.constant.EndpointConstant;
import vip.penint.cloud.common.core.entity.constant.StringConstant;
import vip.penint.cloud.common.security.starter.handler.AuthExceptionEntryPoint;
import vip.penint.cloud.common.security.starter.handler.PenintAccessDeniedHandler;
import vip.penint.cloud.common.security.starter.properties.CloudSecurityProperties;

/**
 *
 */
@EnableResourceServer
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class CloudResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private CloudSecurityProperties properties;
    private PenintAccessDeniedHandler penintAccessDeniedHandler;
    private AuthExceptionEntryPoint exceptionEntryPoint;

    @Autowired(required = false)
    public void setProperties(CloudSecurityProperties properties) {
        this.properties = properties;
    }

    @Autowired(required = false)
    public void setAccessDeniedHandler(PenintAccessDeniedHandler penintAccessDeniedHandler) {
        this.penintAccessDeniedHandler = penintAccessDeniedHandler;
    }

    @Autowired(required = false)
    public void setExceptionEntryPoint(AuthExceptionEntryPoint exceptionEntryPoint) {
        this.exceptionEntryPoint = exceptionEntryPoint;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (properties == null) {
            permitAll(http);
            return;
        }
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUris(), StringConstant.COMMA);
        if (ArrayUtils.isEmpty(anonUrls)) {
            anonUrls = new String[]{};
        }
        if (ArrayUtils.contains(anonUrls, EndpointConstant.ALL)) {
            permitAll(http);
            return;
        }
        http.csrf().disable()
                .requestMatchers().antMatchers(properties.getAuthUri())
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers(properties.getScopeUris()).access("#oauth2.hasScope('" + properties.getScopeName() + "')")
                .antMatchers(properties.getAuthUri()).authenticated()
                .and()
                .httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        if (exceptionEntryPoint != null) {
            resources.authenticationEntryPoint(exceptionEntryPoint);
        }
        if (penintAccessDeniedHandler != null) {
            resources.accessDeniedHandler(penintAccessDeniedHandler);
        }
    }

    private void permitAll(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().permitAll();
    }
}
