package vip.penint.cloud.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.penint.cloud.auth.entity.CustomUser;

import java.util.List;
import java.util.Map;

public interface CustomUserMapper extends BaseMapper<CustomUser> {

    /**
     * 获取用户
     *
     * @param username 用户名
     * @return 用户
     */
    CustomUser findByName(String username);

    List<Map<String, Object>> findLastTenDaysVisitCount();
}
