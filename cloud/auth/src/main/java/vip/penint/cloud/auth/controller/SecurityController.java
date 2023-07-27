package vip.penint.cloud.auth.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.auth.feign.IRemoteUserService;
import vip.penint.cloud.auth.manager.UserManager;
import vip.penint.cloud.auth.service.ValidateCodeService;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.constant.StringConstant;
import vip.penint.cloud.common.core.exception.ValidateCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 *
 */
@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final ValidateCodeService validateCodeService;
    private final ConsumerTokenServices consumerTokenServices;
    private final IRemoteUserService remoteUserService;
    private final UserManager userManager;

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisConnectionFactory connectionFactory;


    @ResponseBody
    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }

    @ResponseBody
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @DeleteMapping("signout")
    public PenintResponse signout(@RequestHeader("Authorization") String token) {
        remoteUserService.deleteUserRedisCache();
        token = StringUtils.replace(token, "bearer ", StringConstant.EMPTY);
        consumerTokenServices.revokeToken(token);
        return new PenintResponse().message("signout");
    }

    /**
     * 重载用户信息到redis
     *
     * @param token
     * @return
     */
    @GetMapping("loadUserByUserToken")
    @ResponseBody
    public PenintResponse loadUserByUserToken(@RequestParam String token) {
        loadByAccessToken(token);
        return new PenintResponse().message("重载用户信息到redis成功");
    }

    /**
     * 还原oauth2 的json格式
     *
     * @return
     */
    public void loadByAccessToken(String token) {
        remoteUserService.deleteUserRedisCache();
        token = StringUtils.replace(token, "bearer ", StringConstant.EMPTY);
        consumerTokenServices.revokeToken(token);

/*        RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
        byte[] serializedKey = serializationStrategy.serialize("auth:" + token);
        RedisConnection conn = connectionFactory.getConnection();
        byte[] bytes;
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
//        OAuth2AccessToken accessToken = serializationStrategy.deserialize(bytes,
//                OAuth2AccessToken.class);

        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        OAuth2Authentication authentication = serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(authentication));
        System.out.println("1---" + jsonObject);

        String permissions = userManager.findUserPermissions(jsonObject.getJSONObject("principal").getString("username"));
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;

        if (StringUtils.isNotBlank(permissions)) {
            grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
        }
        JSONArray authorities = new JSONArray();
        for (GrantedAuthority a : grantedAuthorities) {
            JSONObject data = new JSONObject();
            data.put("authority", a.toString());
            authorities.add(data);
        }

        jsonObject.getJSONObject("principal").put("authorities", authorities);
        System.out.println("2---" + jsonObject);

        OAuth2Authentication authentication1 = (OAuth2Authentication) jsonObject.toJavaObject(Principal.class);

        tokenStore.storeAccessToken(oAuth2AccessToken, authentication1);*/

    }
}
