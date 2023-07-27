package vip.penint.cloud.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.penint.cloud.auth.feign.fallback.RemoteUserServiceFallback;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.entity.constant.PenintServerConstant;
import vip.penint.cloud.common.core.entity.system.SysUser;

/**
 * 
 */
@FeignClient(value = PenintServerConstant.SERVER_SYSTEM, contextId = "userServiceClient", fallbackFactory = RemoteUserServiceFallback.class)
public interface IRemoteUserService {

    /**
     * @return PenintResponse
     */
    @DeleteMapping("adminApi/sysUser/deleteUserRedisCache")
    PenintResponse deleteUserRedisCache();
}
