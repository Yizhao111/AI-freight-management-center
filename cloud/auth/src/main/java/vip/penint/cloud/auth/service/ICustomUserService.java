package vip.penint.cloud.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.auth.entity.CustomUser;
import vip.penint.cloud.common.core.entity.QueryRequest;

import java.util.List;
import java.util.Map;

/**
 * authService接口
 *
 * @author Penint
 * @date 2023-07-17
 */
public interface ICustomUserService extends IService<CustomUser> {


    /**
     * 分页查询auth列表
     *
     * @param customUser   auth
     * @param queryRequest 封装的公共查询条件
     * @return auth集合
     */
    IPage<CustomUser> selectCustomUserList(CustomUser customUser, QueryRequest queryRequest);

    List<Map<String,Object>> findLastTenDaysVisitCount();
}
