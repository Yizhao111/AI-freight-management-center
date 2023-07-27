package vip.penint.cloud.server.busi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.server.busi.entity.ShopCategory;
import vip.penint.cloud.server.busi.service.IShopCategoryService;

import java.util.Arrays;

/**
 * @author Hxx
 * @Description:类目表 控制器
 * @date 2023-07-18
 */
@RestController
@RequestMapping("/shopCategory")
public class ShopCategoryController {
    @Autowired
    private IShopCategoryService shopCategoryService;

    /**
     * 查询类目表列表
     */
    @PreAuthorize("hasAuthority('busi:shopCategory:list')")
    @GetMapping("/list")
    public AjaxResult list(ShopCategory entity) {
        return AjaxResult.success(shopCategoryService.selectShopCategoryList(entity));
    }

    @GetMapping("/l1Options")
    public AjaxResult l1Options() {
        return AjaxResult.success(shopCategoryService.selectL1());
    }

    @GetMapping("/options")
    public AjaxResult options(ShopCategory entity) {
        return AjaxResult.success(shopCategoryService.selectShopCategoryList(entity));
    }

    /**
     * 获取类目表详细信息
     */
    @PreAuthorize("hasAuthority('busi:shopCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(shopCategoryService.getById(id));
    }

    /**
     * 新增类目表
     */
    @PreAuthorize("hasAuthority('busi:shopCategory:add')")
    @PostMapping
    public AjaxResult add(@RequestBody ShopCategory shopCategory) {
        return AjaxResult.success(shopCategoryService.save(shopCategory));
    }

    /**
     * 修改类目表
     */
    @PreAuthorize("hasAuthority('busi:shopCategory:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody ShopCategory shopCategory) {
        return AjaxResult.success(shopCategoryService.updateById(shopCategory));
    }

    /**
     * 删除类目表
     */
    @PreAuthorize("hasAuthority('busi:shopCategory:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return AjaxResult.success(shopCategoryService.removeByIds(Arrays.asList(ids)));
    }

}
