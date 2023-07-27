package vip.penint.cloud.server.busi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.constant.PenintServerConstant;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.busi.feign.fallback.RemoteCustomUserServiceFallback;

/**
 *
 */
@FeignClient(value = PenintServerConstant.AUTH, contextId = "AuthServiceClient", fallbackFactory = RemoteCustomUserServiceFallback.class)
public interface IRemoteCustomUserService {

    @GetMapping("customUser/queryUserId")
    PenintResponse queryCustomUserByUserId(@RequestParam("userId") Integer userId) throws PenintException;

    @GetMapping("customUser/queryCount")
    PenintResponse queryCount() throws PenintException;


}
