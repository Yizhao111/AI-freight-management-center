package vip.penint.cloud.common.security.starter.handler;

import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.utils.PenintUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 */
@Slf4j
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String requestUri = request.getRequestURI();
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String message = "访问令牌不合法";
        log.error("客户端访问{}请求失败: {}", requestUri, message, authException);
        PenintUtil.makeJsonResponse(response, status, new PenintResponse().message(message));
    }
}
