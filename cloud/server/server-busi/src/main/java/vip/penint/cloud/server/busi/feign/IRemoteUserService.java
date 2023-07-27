package vip.penint.cloud.server.busi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.constant.PenintServerConstant;
import vip.penint.cloud.common.core.entity.system.SysUser;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.busi.feign.fallback.RemoteUserServiceFallback;

/**
 *
 */
@FeignClient(value = PenintServerConstant.SERVER_SYSTEM, contextId = "userServiceClient", fallbackFactory = RemoteUserServiceFallback.class)
public interface IRemoteUserService {

    /**
     * remote /user endpoint
     *
     * @param user user
     * @return FebsResponse
     */
    @GetMapping("adminApi/sysUser/list")
    PenintResponse userList(@RequestParam("user") SysUser user) throws PenintException;

}
