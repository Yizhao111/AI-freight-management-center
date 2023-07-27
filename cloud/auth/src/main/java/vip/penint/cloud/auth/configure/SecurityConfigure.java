package vip.penint.cloud.auth.configure;

import org.springframework.security.authentication.AuthenticationProvider;
import vip.penint.cloud.auth.configure.custom.CustomAuthenticationProvider;
import vip.penint.cloud.auth.configure.custom.CustomUserDetailsService;
import vip.penint.cloud.auth.handler.WebLoginFailureHandler;
import vip.penint.cloud.auth.handler.WebLoginSuccessHandler;
import vip.penint.cloud.auth.filter.ValidateCodeFilter;
import vip.penint.cloud.common.core.entity.constant.EndpointConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * WebSecurity配置
 *
 *
 */
@Order(2)
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

//    private final UserDetailsService userDetailService;
    private final CustomUserDetailsService userDetailService;

    private final ValidateCodeFilter validateCodeFilter;
    private final PasswordEncoder passwordEncoder;
    private final WebLoginSuccessHandler successHandler;
    private final WebLoginFailureHandler failureHandler;


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .requestMatchers()
                .antMatchers(EndpointConstant.OAUTH_ALL, EndpointConstant.LOGIN)
                .and()
                .authorizeRequests()
                .antMatchers(EndpointConstant.OAUTH_ALL).authenticated()
                .and()
                .formLogin()
                .loginPage(EndpointConstant.LOGIN)
                .loginProcessingUrl(EndpointConstant.LOGIN)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
                .and().csrf().disable()
                .httpBasic().disable();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
//    }

    /**
     * 不同类型平台用户登陆，
     * 自定义bean之后会走我们定义得CustomAuthenticationProvider，调用CustomUserDetailsService定义的多参loadUserByUsername方法
     * 需要将CustomAuthenticationProvider中默认的UserDetailsService换为我们自己定义的CustomUserDetailsService
     * 不配置，默认走 DaoAuthenticationProvider，然后调用UserDetailsService中的单参数方法loadUserByUsername
     * 不适用于多种类型用户登陆
     * @return
     */
    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider customAuthenticationProvider= new CustomAuthenticationProvider();
        customAuthenticationProvider.setUserDetailsService(userDetailService);
        customAuthenticationProvider.setHideUserNotFoundExceptions(false);
        customAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return customAuthenticationProvider;
    }

    /**
     * 不同类型平台用户登陆
     * @param auth
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(customAuthenticationProvider());
    }
}
