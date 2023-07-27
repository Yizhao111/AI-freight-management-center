package vip.penint.cloud.common.security.starter.interceptor;

import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.constant.PenintConstant;
import vip.penint.cloud.common.core.utils.PenintUtil;
import vip.penint.cloud.common.security.starter.properties.CloudSecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 */
public class ServerProtectInterceptor implements HandlerInterceptor {

    private CloudSecurityProperties properties;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        if (!properties.getOnlyFetchByGateway()) {
            return true;
        }
        String token = request.getHeader(PenintConstant.GATEWAY_TOKEN_HEADER);
        String gatewayToken = new String(Base64Utils.encode(PenintConstant.GATEWAY_TOKEN_VALUE.getBytes()));
        if (StringUtils.equals(gatewayToken, token)) {
            return true;
        } else {
            PenintResponse penintResponse = new PenintResponse();
            PenintUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, penintResponse.message("请通过网关获取资源"));
            return false;
        }
    }

    public void setProperties(CloudSecurityProperties properties) {
        this.properties = properties;
    }
}
