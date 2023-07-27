package vip.penint.cloud.auth.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vip.penint.cloud.auth.entity.CustomUser;
import vip.penint.cloud.auth.mapper.CustomUserMapper;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CustomUserManager {

    private final CustomUserMapper customUserMapper;
    private final PasswordEncoder passwordEncoder;
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户
     */
    public CustomUser findByName(String username) {
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
        return customUserMapper.findByName(username);
    }
}
