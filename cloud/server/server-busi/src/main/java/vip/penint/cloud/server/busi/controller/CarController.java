package vip.penint.cloud.server.busi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.server.busi.entity.Car;
import vip.penint.cloud.server.busi.service.ICarService;

import java.util.Arrays;

/**
 * @author Hxx
 * @Description:车辆管理 控制器
 * @date 2023-07-18
 */
@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private ICarService carService;

    /**
     * 查询车辆管理列表
     */
    @PreAuthorize("hasAuthority('busi:car:list')")
    @GetMapping("/list")
    public AjaxResult list(Car entity, QueryRequest queryRequest) {
        return AjaxResult.success(carService.selectCarList(entity, queryRequest));
    }

    @GetMapping("/listAll")
    public AjaxResult listAll() {
        LambdaQueryWrapper<Car> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Car::getStatus,2);
        return AjaxResult.success(carService.list(wrapper));
    }


    /**
     * 获取车辆管理详细信息
     */
    @PreAuthorize("hasAuthority('busi:car:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(carService.getById(id));
    }

    /**
     * 新增车辆管理
     */
    @PreAuthorize("hasAuthority('busi:car:add')")
    @PostMapping
    public AjaxResult add(@RequestBody Car car) {
        return AjaxResult.success(carService.save(car));
    }

    /**
     * 修改车辆管理
     */
    @PreAuthorize("hasAuthority('busi:car:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody Car car) {
        return AjaxResult.success(carService.updateById(car));
    }

    /**
     * 删除车辆管理
     */
    @PreAuthorize("hasAuthority('busi:car:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return AjaxResult.success(carService.removeByIds(Arrays.asList(ids)));
    }

}
