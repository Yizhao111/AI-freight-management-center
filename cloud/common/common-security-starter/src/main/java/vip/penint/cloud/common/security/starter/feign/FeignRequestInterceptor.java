package vip.penint.cloud.common.security.starter.feign;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import vip.penint.cloud.common.core.entity.constant.SecurityConstants;
//import vip.penint.cloud.common.core.utils.IpUtils;
//import vip.penint.cloud.common.core.utils.PenintUtil;
//import vip.penint.cloud.common.core.utils.ServletUtils;
//import vip.penint.cloud.common.core.utils.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
///**
// * feign 请求拦截器
// */
//@Component
//public class FeignRequestInterceptor implements RequestInterceptor {
//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
//        if (StringUtils.isNotNull(httpServletRequest)) {
//            Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);
//            // 传递用户信息请求头，防止丢失
//            String userId = headers.get(SecurityConstants.DETAILS_USER_ID);
//            if (StringUtils.isNotEmpty(userId)) {
//                requestTemplate.header(SecurityConstants.DETAILS_USER_ID, userId);
//            }
//            String userName = headers.get(SecurityConstants.DETAILS_USERNAME);
//            if (StringUtils.isNotEmpty(userName)) {
//                requestTemplate.header(SecurityConstants.DETAILS_USERNAME, userName);
//            }
//            String authentication = headers.get(SecurityConstants.AUTHORIZATION_HEADER);
//            if (StringUtils.isNotEmpty(authentication)) {
//                requestTemplate.header(SecurityConstants.AUTHORIZATION_HEADER, authentication);
//            }
//
//            // 配置客户端IP
//            requestTemplate.header("X-Forwarded-For", IpUtils.getIpAddr(ServletUtils.getRequest()));
//        }
//    }
//}

/**
 * Description:
 * 微服务之间feign调用请求头丢失的问题
 */

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (httpServletRequest != null) {
            Map<String, String> headers = getHeaders(httpServletRequest);
            // 传递所有请求头,防止部分丢失
            //此处也可以只传递认证的header
//            template.header("Authorization", request.getHeader("Authorization"));
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                template.header(entry.getKey(), entry.getValue());
            }
            log.debug("FeignRequestInterceptor:{}", template.toString());
        }
    }


    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取原请求头
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }
        return map;
    }

}
