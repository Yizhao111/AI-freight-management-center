//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package vip.penint.cloud.auth.configure.custom;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.util.Assert;

@Configuration
@EnableAuthorizationServer
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
    private PasswordEncoder passwordEncoder;
    private volatile String userNotFoundEncodedPassword;
    private CustomUserDetailsService userDetailsService;
    private UserDetailsPasswordService userDetailsPasswordService;


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }

    @Override
    protected void doAfterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        this.prepareTimingAttackProtection();
        Object details = authentication.getDetails();
        JSONObject detailsJson = JSONObject.parseObject(JSONObject.toJSONString(details));
        try {
            if (StringUtils.isNotBlank(detailsJson.getString("sessionId")) || StringUtils.isNotBlank(detailsJson.getString("remoteAddress"))) {
                // 授权码模式/简单模式 进入该loadUserByUsername
                // 授权码模式detailsJson 带sessionId,简单模式 detailsJson带remoteAddress
                UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username); // 自定义添加userType参数
                if (loadedUser == null) {
                    throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
                } else {
                    return loadedUser;
                }

            } else {
                // 密码模式进入该方法
                String userType = detailsJson.getString("userType"); // 自定义添加
                UserDetails loadedUser;
                // 处理第三方登录无userType
                if (StringUtils.isBlank(userType)){
                    loadedUser = this.getUserDetailsService().loadUserByUsername(username);
                }else {
                    loadedUser = this.getUserDetailsService().loadUserByUsername(username, userType); // 自定义添加userType参数
                }
                if (loadedUser == null) {
                    throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
                } else {
                    return loadedUser;
                }
            }
        } catch (UsernameNotFoundException var4) {
            this.mitigateAgainstTimingAttack(authentication);
            throw var4;
        } catch (InternalAuthenticationServiceException var5) {
            throw var5;
        } catch (Exception var6) {
            throw new InternalAuthenticationServiceException(var6.getMessage(), var6);
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }

        return super.createSuccessAuthentication(principal, authentication, user);
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode("userNotFoundPassword");
        }

    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }

    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    /**
     * 这里改为自定义的CustomUserDetailsService
     */
    public void setUserDetailsService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 这里改为自定义的CustomUserDetailsService
     */
    protected CustomUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
        this.userDetailsPasswordService = userDetailsPasswordService;
    }
}
