package vip.penint.cloud.common.security.starter.handler;

import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.utils.PenintUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class PenintAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        PenintResponse penintResponse = new PenintResponse();
        PenintUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, penintResponse.message("没有权限访问该资源"));
    }
}
