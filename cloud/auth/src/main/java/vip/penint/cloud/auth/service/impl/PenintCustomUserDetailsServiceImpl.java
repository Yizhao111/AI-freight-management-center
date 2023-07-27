package vip.penint.cloud.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vip.penint.cloud.auth.configure.custom.CustomUserDetailsService;
import vip.penint.cloud.auth.entity.CustomUser;
import vip.penint.cloud.auth.manager.CustomUserManager;
import vip.penint.cloud.auth.manager.UserManager;
import vip.penint.cloud.common.core.entity.PenintAuthUser;
import vip.penint.cloud.common.core.entity.constant.ParamsConstant;
import vip.penint.cloud.common.core.entity.constant.SocialConstant;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.utils.PenintUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PenintCustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserManager userManager;
    private final CustomUserManager customUserManager;

    @Override
    public UserDetails loadUserByUsername(String username, String userType) throws UsernameNotFoundException {
        if (StringUtils.equals(userType, SocialConstant.SYS_LOGIN)) {
            SysUser systemUser = userManager.findByName(username);
            if (systemUser != null) {
                String permissions = userManager.findUserPermissions(systemUser.getUsername());
                boolean notLocked = false;
                if (SysUser.STATUS_VALID.equals(systemUser.getStatus())) {
                    notLocked = true;
                }
                String password = systemUser.getPassword();
//                String password = passwordEncoder.encode(SocialConstant.getSocialLoginPassword());

                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
                if (StringUtils.isNotBlank(permissions)) {
                    grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
                }
                PenintAuthUser authUser = new PenintAuthUser(systemUser.getUsername(), password, true, true, true, notLocked,
                        grantedAuthorities);

                BeanUtils.copyProperties(systemUser, authUser);
                return authUser;
            } else {
                throw new UsernameNotFoundException("用户名或密码错误");
            }
        }
        if (StringUtils.equals(userType, SocialConstant.CUSTOM_LOGIN)) {
            CustomUser customUser = customUserManager.findByName(username);
            if (customUser != null) {
                boolean notLocked = true;
//                if (SysUser.STATUS_VALID.equals(customUser.getStatus())) {
//                    notLocked = true;
//                }
//                String password = passwordEncoder.encode(SocialConstant.getSocialLoginPassword());
                String password = customUser.getPassword();

                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
                PenintAuthUser authUser = new PenintAuthUser(customUser.getUsername(), password, true, true, true, notLocked,
                        grantedAuthorities);

                BeanUtils.copyProperties(customUser, authUser);
                return authUser;
            } else {
                throw new UsernameNotFoundException("用户名或密码错误");
            }
        }
        throw new UsernameNotFoundException("没有匹配的登陆类型:userType");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest httpServletRequest = PenintUtil.getHttpServletRequest();
        SysUser systemUser = userManager.findByName(username);
        if (systemUser != null) {
            String permissions = userManager.findUserPermissions(systemUser.getUsername());
            boolean notLocked = false;
            if (SysUser.STATUS_VALID.equals(systemUser.getStatus())) {
                notLocked = true;
            }
            String password = systemUser.getPassword();
//                String password = passwordEncoder.encode(SocialConstant.getSocialLoginPassword());
            String loginType = (String) httpServletRequest.getAttribute(ParamsConstant.LOGIN_TYPE);

            if (StringUtils.equals(loginType, SocialConstant.SOCIAL_LOGIN)) {
                password = passwordEncoder.encode(SocialConstant.getSocialLoginPassword());
            }
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
            if (StringUtils.isNotBlank(permissions)) {
                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
            }
            PenintAuthUser authUser = new PenintAuthUser(systemUser.getUsername(), password, true, true, true, notLocked,
                    grantedAuthorities);

            BeanUtils.copyProperties(systemUser, authUser);
            return authUser;
        } else {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
    }
}
