package vip.penint.cloud.server.busi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.server.busi.entity.Orders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
public interface IOrdersService extends IService<Orders> {

    /**
     * 分页查询列表
     *
     * @param entity
     * @param queryRequest 封装的公共查询条件
     * @return 集合
     */
    IPage<Orders> selectOrdersList(Orders entity, QueryRequest queryRequest);

    void saveOrders(Orders orders);

    Orders getInfoById(Integer orderId);

    void updateOrders(Orders orders);

    List<Map<String, Object>> findLastTenDaysVisitCount();

    BigDecimal orderPrice();

}
