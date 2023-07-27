package vip.penint.cloud.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.penint.cloud.auth.entity.CustomUser;
import vip.penint.cloud.auth.mapper.CustomUserMapper;
import vip.penint.cloud.auth.service.ICustomUserService;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * authService业务层处理
 *
 * @author Penint
 * @date 2023-07-17
 */
@Service
public class CustomUserServiceImpl extends ServiceImpl<CustomUserMapper, CustomUser> implements ICustomUserService {
    @Autowired
    private CustomUserMapper customUserMapper;


    /**
     * 分页查询auth列表
     *
     * @param customUser auth
     * @return auth
     */
    @Override
    public IPage<CustomUser> selectCustomUserList(CustomUser customUser, QueryRequest queryRequest) {
        IPage<CustomUser> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        LambdaQueryWrapper<CustomUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .like(StringUtils.isNotEmpty(customUser.getRealName()), CustomUser::getRealName, customUser.getRealName())
                .like(StringUtils.isNotEmpty(customUser.getCompanyName()), CustomUser::getCompanyName, customUser.getCompanyName())
                .like(StringUtils.isNotEmpty(customUser.getPhone()), CustomUser::getPhone, customUser.getPhone())
                .like(StringUtils.isNotEmpty(customUser.getIdCard()), CustomUser::getIdCard, customUser.getIdCard())
                .orderByDesc(CustomUser::getCreateTime);

        return customUserMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public List<Map<String, Object>> findLastTenDaysVisitCount() {
        return customUserMapper.findLastTenDaysVisitCount();
    }
}
