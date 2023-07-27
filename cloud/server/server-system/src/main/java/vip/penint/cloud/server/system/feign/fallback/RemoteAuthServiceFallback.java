package vip.penint.cloud.server.system.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import vip.penint.cloud.common.core.annotation.Fallback;
import vip.penint.cloud.server.system.feign.IRemoteAuthService;

/**
 * 
 */
@Slf4j
@Fallback
public class RemoteAuthServiceFallback implements FallbackFactory<IRemoteAuthService> {

    @Override
    public IRemoteAuthService create(Throwable throwable) {
        return (token) -> {
            log.error("重载用户信息失败", throwable);
            return null;
        };
    }
}