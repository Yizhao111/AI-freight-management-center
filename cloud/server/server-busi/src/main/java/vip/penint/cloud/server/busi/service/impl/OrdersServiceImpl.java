package vip.penint.cloud.server.busi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.common.core.utils.StringUtils;
import vip.penint.cloud.server.busi.entity.*;
import vip.penint.cloud.server.busi.feign.IRemoteCustomUserService;
import vip.penint.cloud.server.busi.mapper.IOrdersMapper;
import vip.penint.cloud.server.busi.service.*;
import vip.penint.cloud.server.busi.utils.SnowFlake;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Hxx
 * @since 2023-07-18
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<IOrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private IOrdersGoodsService ordersGoodsService;
    @Autowired
    private IShopGoodsService goodsService;

    @Autowired
    private IRemoteCustomUserService remoteCustomUserService;

    @Autowired
    private ICarService carService;

    @Autowired
    private IFinanceService financeService;

    @Resource
    private IOrdersMapper ordersMapper;

    @Override
    public IPage<Orders> selectOrdersList(Orders entity, QueryRequest queryRequest) {
        IPage<Orders> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize(), true);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(entity.getOrderSn()), Orders::getOrderSn, entity.getOrderSn())
                .eq(StringUtils.isNotEmpty(entity.getStatus()), Orders::getStatus, entity.getStatus())
                .orderByDesc(Orders::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrders(Orders orders) {
        SnowFlake idWorker = new SnowFlake(1, 1);
        orders.setOrderSn("SN" + idWorker.nextId());
        // 微服务远程调用
        PenintResponse penintResponse = null;
        try {
            penintResponse = remoteCustomUserService.queryCustomUserByUserId(orders.getCustomUserId());
        } catch (PenintException e) {
            throw new RuntimeException(e);
        }
        Object data = penintResponse.getData();
        JSONObject remoteData = JSONObject.parseObject(JSONObject.toJSONString(data));
        orders.setPhoneNumber(remoteData.getString("phone"));
        orders.setRecipientName(remoteData.getString("realName"));
        baseMapper.insert(orders);
        List<OrdersGoods> goodsList = orders.getGoodsList();
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrdersGoods ordersGoods : goodsList) {
            Integer goodsId = ordersGoods.getGoodsId();
            Integer number = ordersGoods.getNumber();
            ShopGoods goods = goodsService.getById(goodsId);
            ordersGoods.setGoodsName(goods.getName());
            ordersGoods.setOrderId(orders.getOrderId());
            ordersGoods.setPrice(goods.getPrice());
            BigDecimal goodsPrice = BigDecimal.valueOf(number).multiply(goods.getPrice());
            totalPrice = totalPrice.add(goodsPrice);
            ordersGoodsService.save(ordersGoods);
        }
        orders.setTotalPrice(totalPrice);
        baseMapper.updateById(orders);
    }

    @Override
    public void updateOrders(Orders orders) {
        List<OrdersGoods> goodsList = orders.getGoodsList();
        if (goodsList != null && goodsList.size() > 0) {
            // 微服务远程调用
            PenintResponse penintResponse = null;
            try {
                penintResponse = remoteCustomUserService.queryCustomUserByUserId(orders.getCustomUserId());
            } catch (PenintException e) {
                throw new RuntimeException(e);
            }
            Object data = penintResponse.getData();
            JSONObject remoteData = JSONObject.parseObject(JSONObject.toJSONString(data));
            orders.setPhoneNumber(remoteData.getString("phone"));
            orders.setRecipientName(remoteData.getString("realName"));
            BigDecimal totalPrice = new BigDecimal(0);
            // 通过订单号删除订单商品
            ordersGoodsService.removeByOrderId(orders.getOrderId());
            for (OrdersGoods ordersGoods : goodsList) {
                Integer goodsId = ordersGoods.getGoodsId();
                Integer number = ordersGoods.getNumber();
                ShopGoods goods = goodsService.getById(goodsId);
                ordersGoods.setGoodsName(goods.getName());
                ordersGoods.setOrderId(orders.getOrderId());
                ordersGoods.setPrice(goods.getPrice());
                BigDecimal goodsPrice = BigDecimal.valueOf(number).multiply(goods.getPrice());
                totalPrice = totalPrice.add(goodsPrice);
                ordersGoodsService.save(ordersGoods);
            }
            orders.setTotalPrice(totalPrice);
        }

        if (StringUtils.isNotNull(orders.getCarId())) {
            Car car = carService.getById(orders.getCarId());
            orders.setCarNum(car.getCarNum());
            if (orders.getStatus().equals("1")) {
                car.setStatus(1);
                carService.updateById(car);
            }
        }
        if (orders.getStatus().equals("3")) {
            Orders or = baseMapper.selectById(orders.getOrderId());
            Car car = new Car();
            car.setId(or.getCarId());
            car.setStatus(2);
            carService.updateById(car);
            // 财务收入
            Finance finance = new Finance();
            finance.setOrderNo(or.getOrderSn());
            finance.setType(1);
            finance.setAmount(or.getTotalPrice());
            finance.setCreateTime(LocalDateTime.now());
            financeService.save(finance);
        }
        if (orders.getStatus().equals("4")) {
            Orders or = baseMapper.selectById(orders.getOrderId());
            // 财务支出
            Finance finance = new Finance();
            finance.setOrderNo(or.getOrderSn());
            finance.setType(2);
            finance.setAmount(new BigDecimal(0).subtract(or.getTotalPrice()));
            finance.setCreateTime(LocalDateTime.now());
            financeService.save(finance);
        }
        baseMapper.updateById(orders);
    }

    @Override
    public Orders getInfoById(Integer orderId) {
        Orders orders = baseMapper.selectById(orderId);
        orders.setGoodsList(ordersGoodsService.selectByOrderId(orderId));
        return orders;
    }

    @Override
    public List<Map<String, Object>> findLastTenDaysVisitCount() {
        return ordersMapper.findLastTenDaysVisitCount();
    }

    @Override
    public BigDecimal orderPrice() {
        return ordersMapper.orderPrice();
    }
}
