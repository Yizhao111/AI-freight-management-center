package vip.penint.cloud.server.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.constant.PenintServerConstant;
import vip.penint.cloud.server.system.feign.fallback.RemoteAuthServiceFallback;

/**
 *
 */
@FeignClient(value = PenintServerConstant.AUTH, contextId = "AuthServiceClient", fallbackFactory = RemoteAuthServiceFallback.class)
public interface IRemoteAuthService {

    /**
     *
     */
    @GetMapping("loadUserByUserToken")
    PenintResponse loadUserByUserToken(@RequestParam("token") String token);

}
