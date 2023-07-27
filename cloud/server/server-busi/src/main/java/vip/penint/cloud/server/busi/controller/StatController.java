package vip.penint.cloud.server.busi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.server.busi.feign.IRemoteCustomUserService;
import vip.penint.cloud.server.busi.service.ICarService;
import vip.penint.cloud.server.busi.service.IOrdersService;
import vip.penint.cloud.server.busi.service.IShopCategoryService;
import vip.penint.cloud.server.busi.service.IShopGoodsService;

import java.math.BigDecimal;

/**
 * @author Hxx
 * @Description:财务表 控制器
 * @date 2023-07-19
 */
@RestController
@RequestMapping("/stat")
public class StatController {
    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IShopGoodsService goodsService;

    @Autowired
    private ICarService carService;

    @Autowired
    private IShopCategoryService categoryService;


    @Autowired
    private IRemoteCustomUserService remoteCustomUserService;

    @GetMapping("/count")
    public AjaxResult count() throws PenintException {
        // 订单数
        int orderCount = ordersService.count();
        // 订单金额
        BigDecimal orderPrice = ordersService.orderPrice();
        // 商品数
        int goodsCount = goodsService.count();
        // 车辆数
        int carCount = carService.count();
        // 客户数
        int customCount = (int) remoteCustomUserService.queryCount().getData();
        JSONObject data = new JSONObject();
        data.put("orderCount", orderCount);
        data.put("goodsCount", goodsCount);
        data.put("carCount", carCount);
        data.put("customCount", customCount);
        data.put("orderPrice", orderPrice);
        return AjaxResult.success(data);
    }


    // 左边第一个图
    @GetMapping("/leftChat1")
    public AjaxResult leftChat1() {
        return AjaxResult.success(categoryService.findCateGoods());
    }

    // 左边第二个图
    @GetMapping("/leftChat2")
    public AjaxResult leftChat2() {
        return AjaxResult.success(ordersService.findLastTenDaysVisitCount());
    }

    // 实时订单
    @GetMapping("/listOrderStat")
    public AjaxResult listOrderStat() {
        return AjaxResult.success(ordersService.list());
    }

    // 实时商品
    @GetMapping("/listGoodsStat")
    public AjaxResult listGoodsStat() {
        return AjaxResult.success(goodsService.list());
    }
}
