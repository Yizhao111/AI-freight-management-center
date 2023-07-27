package vip.penint.cloud.server.ques.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import vip.penint.cloud.common.core.annotation.Fallback;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.ques.feign.IRemoteCustomUserService;

/**
 *
 */
@Slf4j
@Fallback
public class RemoteCustomUserServiceFallback implements FallbackFactory<IRemoteCustomUserService> {


    @SneakyThrows
    @Override
    public IRemoteCustomUserService create(Throwable throwable) {
        log.error("获取客户信息失败", throwable);
        throw new PenintException(throwable.getMessage());
    }
}
