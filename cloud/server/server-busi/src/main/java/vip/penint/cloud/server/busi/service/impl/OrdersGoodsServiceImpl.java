package vip.penint.cloud.server.busi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.server.busi.entity.OrdersGoods;
import vip.penint.cloud.server.busi.mapper.IOrdersGoodsMapper;
import vip.penint.cloud.server.busi.service.IOrdersGoodsService;

import java.util.List;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
@Service
public class OrdersGoodsServiceImpl extends ServiceImpl<IOrdersGoodsMapper, OrdersGoods> implements IOrdersGoodsService {

    @Override
    public IPage<OrdersGoods> selectOrdersGoodsList(OrdersGoods entity, QueryRequest queryRequest) {
        IPage<OrdersGoods> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        LambdaQueryWrapper<OrdersGoods> wrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<OrdersGoods> selectByOrderId(Integer orderId) {
        LambdaQueryWrapper<OrdersGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersGoods::getOrderId, orderId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void removeByOrderId(Integer orderId) {
        LambdaQueryWrapper<OrdersGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrdersGoods::getOrderId, orderId);
        baseMapper.delete(wrapper);
    }
}
