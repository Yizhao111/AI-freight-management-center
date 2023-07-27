package vip.penint.cloud.auth.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import vip.penint.cloud.auth.feign.IRemoteUserService;
import vip.penint.cloud.common.core.annotation.Fallback;

/**
 * 
 */
@Slf4j
@Fallback
public class RemoteUserServiceFallback implements FallbackFactory<IRemoteUserService> {

    @Override
    public IRemoteUserService create(Throwable throwable) {
        return () -> {
            log.error("获取用户信息失败", throwable);
            return null;
        };
    }
}