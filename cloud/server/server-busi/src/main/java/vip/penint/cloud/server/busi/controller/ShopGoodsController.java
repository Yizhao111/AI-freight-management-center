package vip.penint.cloud.server.busi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.server.busi.entity.ShopGoods;
import vip.penint.cloud.server.busi.service.IShopGoodsService;
import vip.penint.cloud.server.busi.utils.SnowFlake;

import java.util.Arrays;

/**
 * @author Hxx
 * @Description:商品表 控制器
 * @date 2023-07-18
 */
@RestController
@RequestMapping("/shopGoods")
public class ShopGoodsController {
    @Autowired
    private IShopGoodsService shopGoodsService;

    /**
     * 查询商品表列表
     */
    @PreAuthorize("hasAuthority('busi:shopGoods:list')")
    @GetMapping("/list")
    public AjaxResult list(ShopGoods entity, QueryRequest queryRequest) {
        return AjaxResult.success(shopGoodsService.selectShopGoodsList(entity, queryRequest));
    }

    /**
     * 获取商品表详细信息
     */
    @PreAuthorize("hasAuthority('busi:shopGoods:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(shopGoodsService.getById(id));
    }

    /**
     * 新增商品表
     */
    @PreAuthorize("hasAuthority('busi:shopGoods:add')")
    @PostMapping
    public AjaxResult add(@RequestBody ShopGoods shopGoods) {
        SnowFlake idWorker = new SnowFlake(0, 0);
        shopGoods.setGoodsSn("SP" + idWorker.nextId());
        return AjaxResult.success(shopGoodsService.save(shopGoods));
    }

    /**
     * 修改商品表
     */
    @PreAuthorize("hasAuthority('busi:shopGoods:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody ShopGoods shopGoods) {
        return AjaxResult.success(shopGoodsService.updateById(shopGoods));
    }

    /**
     * 删除商品表
     */
    @PreAuthorize("hasAuthority('busi:shopGoods:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return AjaxResult.success(shopGoodsService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 获取所有商品
     */
    @GetMapping("/listAll")
    public AjaxResult listAll() {
        return AjaxResult.success(shopGoodsService.list());
    }
}
