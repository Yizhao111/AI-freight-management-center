package vip.penint.cloud.auth.filter;

import vip.penint.cloud.auth.service.ValidateCodeService;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.constant.EndpointConstant;
import vip.penint.cloud.common.core.entity.constant.GrantTypeConstant;
import vip.penint.cloud.common.core.entity.constant.ParamsConstant;
import vip.penint.cloud.common.core.entity.constant.SocialConstant;
import vip.penint.cloud.common.core.exception.ValidateCodeException;
import vip.penint.cloud.common.core.utils.PenintUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 *
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final ValidateCodeService validateCodeService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest httpServletRequest, @Nonnull HttpServletResponse httpServletResponse,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        RequestMatcher matcher = new AntPathRequestMatcher(EndpointConstant.OAUTH_TOKEN, HttpMethod.POST.toString());
        if (matcher.matches(httpServletRequest)
                && StringUtils.equalsIgnoreCase(httpServletRequest.getParameter(ParamsConstant.GRANT_TYPE), GrantTypeConstant.PASSWORD)) {
            try {
                validateCode(httpServletRequest);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (Exception e) {
                PenintResponse PenintResponse = new PenintResponse();
                PenintUtil.makeFailureResponse(httpServletResponse, PenintResponse.message(e.getMessage()));
                log.error(e.getMessage(), e);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private void validateCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
        String code = httpServletRequest.getParameter(ParamsConstant.VALIDATE_CODE_CODE);
        String key = httpServletRequest.getParameter(ParamsConstant.VALIDATE_CODE_KEY);
        String userType = httpServletRequest.getParameter(ParamsConstant.USER_TYPE);
        // TODO 取消验证前台用户验证码 或登录三次失败需要验证码，可通过ip判断
        if (!StringUtils.equals(userType, SocialConstant.CUSTOM_LOGIN) && StringUtils.isNotBlank(userType)){
            validateCodeService.check(key, code);
        }
    }
}
