package vip.penint.cloud.server.busi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.server.busi.entity.OrdersGoods;

import java.util.List;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
public interface IOrdersGoodsService extends IService<OrdersGoods> {

    /**
     * 分页查询订单商品表列表
     *
     * @param entity       订单商品表
     * @param queryRequest 封装的公共查询条件
     * @return 订单商品表集合
     */
    IPage<OrdersGoods> selectOrdersGoodsList(OrdersGoods entity, QueryRequest queryRequest);

    List<OrdersGoods> selectByOrderId(Integer orderId);

    void removeByOrderId(Integer orderId);
}
