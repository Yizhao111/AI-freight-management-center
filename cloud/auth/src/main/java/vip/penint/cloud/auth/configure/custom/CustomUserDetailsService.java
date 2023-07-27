package vip.penint.cloud.auth.configure.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {

    /**
     * 重写loadUserByUsername方法，实现多用户登录
     * @param username
     * @param userType
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByUsername(String username,String userType) throws UsernameNotFoundException;

    /**
     * 授权码模式专用
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
