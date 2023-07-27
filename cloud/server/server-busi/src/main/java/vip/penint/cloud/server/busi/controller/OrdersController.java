package vip.penint.cloud.server.busi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.server.busi.entity.LnglatVo;
import vip.penint.cloud.server.busi.entity.Orders;
import vip.penint.cloud.server.busi.service.IOrdersService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hxx
 * @Description: 控制器
 * @date 2023-07-18
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private IOrdersService ordersService;

    /**
     * 查询列表
     */
    @PreAuthorize("hasAuthority('busi:orders:list')")
    @GetMapping("/list")
    public AjaxResult list(Orders entity, QueryRequest queryRequest) {
        return AjaxResult.success(ordersService.selectOrdersList(entity, queryRequest));
    }


    /**
     * 获取详细信息
     */
    @PreAuthorize("hasAuthority('busi:orders:query')")
    @GetMapping(value = "/{order_id}")
    public AjaxResult getInfo(@PathVariable("order_id") Integer order_id) {
        return AjaxResult.success(ordersService.getInfoById(order_id));
    }

    /**
     * 新增
     */
    @PreAuthorize("hasAuthority('busi:orders:add')")
    @PostMapping
    public AjaxResult add(@RequestBody Orders orders) {
        ordersService.saveOrders(orders);
        return AjaxResult.success();
    }

    /**
     * 修改
     */
    @PreAuthorize("hasAuthority('busi:orders:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody Orders orders) {
        ordersService.updateOrders(orders);
        return AjaxResult.success();
    }

    /**
     * 删除
     */
    @PreAuthorize("hasAuthority('busi:orders:remove')")
    @DeleteMapping("/{order_ids}")
    public AjaxResult remove(@PathVariable Integer[] order_ids) {
        return AjaxResult.success(ordersService.removeByIds(Arrays.asList(order_ids)));
    }


    /**
     * 获取所有的经纬度坐标，完成点聚合地图
     * [
     * { weight: 1, lnglat: ["108.939621", "34.343147"] },
     * { weight: 1, lnglat: ["108.939621", "34.343147"] },
     * { weight: 1, lnglat: ["108.939621", "34.343147"] },
     * { weight: 1, lnglat: ["108.939621", "34.343147"] },
     * { weight: 1, lnglat: ["108.939621", "34.343147"] },
     * { weight: 1, lnglat: ["108.939621", "34.343147"] },
     * { weight: 1, lnglat: ["113.370643", "22.938827"] },
     * { weight: 1, lnglat: ["112.985037", "23.15046"] },
     * { weight: 1, lnglat: ["112.985037", "23.15046"] },
     * { weight: 1, lnglat: ["112.985037", "23.15046"] },
     * { weight: 1, lnglat: ["112.985037", "23.15046"] },
     * { weight: 1, lnglat: ["110.361899", "20.026695"] },
     * { weight: 1, lnglat: ["110.361899", "20.026695"] },
     * { weight: 1, lnglat: ["110.361899", "20.026695"] },
     * { weight: 1, lnglat: ["110.361899", "20.026695"] },
     * { weight: 1, lnglat: ["110.361899", "20.026695"] },
     * { weight: 1, lnglat: ["121.434529", "31.215641"] },
     * ];
     */
    @GetMapping("/getLnglat")
    public AjaxResult getLnglat() {
        List<Orders> list = ordersService.list();
        List<LnglatVo> result = list.stream().map(order -> {
            LnglatVo vo = new LnglatVo();
            vo.setLnglat(new String[]{order.getLng().toString(), order.getLat().toString()});
            return vo;
        }).collect(Collectors.toList());
        return AjaxResult.success(result);
    }
}
