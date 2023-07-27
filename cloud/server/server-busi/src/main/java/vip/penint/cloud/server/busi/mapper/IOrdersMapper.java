package vip.penint.cloud.server.busi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.penint.cloud.server.busi.entity.Orders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
public interface IOrdersMapper extends BaseMapper<Orders> {

    List<Map<String, Object>> findLastTenDaysVisitCount();

    BigDecimal orderPrice();
}
