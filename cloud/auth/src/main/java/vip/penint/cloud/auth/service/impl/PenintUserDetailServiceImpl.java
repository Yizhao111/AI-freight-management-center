//package vip.penint.cloud.auth.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeanUtils;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import vip.penint.cloud.auth.manager.UserManager;
//import vip.penint.cloud.common.core.entity.PenintAuthUser;
//import vip.penint.cloud.common.core.entity.constant.ParamsConstant;
//import vip.penint.cloud.common.core.entity.constant.SocialConstant;
//import vip.penint.cloud.common.core.entity.system.SysUser;
//import vip.penint.cloud.common.core.utils.PenintUtil;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
///**
// *
// */
//@Service
//@RequiredArgsConstructor
//public class PenintUserDetailServiceImpl implements UserDetailsService {
//
//    private final PasswordEncoder passwordEncoder;
//    private final UserManager userManager;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        HttpServletRequest httpServletRequest = PenintUtil.getHttpServletRequest();
//        SysUser systemUser = userManager.findByName(username);
//        if (systemUser != null) {
//            String permissions = userManager.findUserPermissions(systemUser.getUsername());
//            boolean notLocked = false;
//            if (SysUser.STATUS_VALID.equals(systemUser.getStatus())) {
//                notLocked = true;
//            }
//            String password = systemUser.getPassword();
//            String loginType = (String) httpServletRequest.getAttribute(ParamsConstant.SYS_LOGIN_TYPE);
//            if (StringUtils.equals(loginType, SocialConstant.SYS_LOGIN)) {
//                password = passwordEncoder.encode(SocialConstant.getSocialLoginPassword());
//            }
//
//            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
//            if (StringUtils.isNotBlank(permissions)) {
//                grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
//            }
//            PenintAuthUser authUser = new PenintAuthUser(systemUser.getUsername(), password, true, true, true, notLocked,
//                    grantedAuthorities);
//
//            BeanUtils.copyProperties(systemUser, authUser);
//            return authUser;
//        } else {
//            throw new UsernameNotFoundException("");
//        }
//    }
//}
