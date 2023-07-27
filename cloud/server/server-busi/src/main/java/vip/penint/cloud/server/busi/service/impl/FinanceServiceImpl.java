package vip.penint.cloud.server.busi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.utils.StringUtils;
import vip.penint.cloud.server.busi.entity.Finance;
import vip.penint.cloud.server.busi.mapper.IFinanceMapper;
import vip.penint.cloud.server.busi.service.IFinanceService;

/**
 * <p>
 * 财务表 服务实现类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-19
 */
@Service
public class FinanceServiceImpl extends ServiceImpl<IFinanceMapper, Finance> implements IFinanceService {

    @Override
    public IPage<Finance> selectFinanceList(Finance entity, QueryRequest queryRequest) {
        IPage<Finance> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        LambdaQueryWrapper<Finance> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(StringUtils.isNotEmpty(entity.getOrderNo()), Finance::getOrderNo, entity.getOrderNo())
                .orderByDesc(Finance::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }
}
