package vip.penint.cloud.server.busi.feign.fallback;

import vip.penint.cloud.common.core.annotation.Fallback;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.busi.feign.IRemoteUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Fallback
public class RemoteUserServiceFallback implements FallbackFactory<IRemoteUserService> {

    @Override
    public IRemoteUserService create(Throwable throwable) {
        return (user) -> {
            log.error("获取用户信息失败", throwable);
            throw new PenintException(throwable.getMessage());
        };
    }


}
