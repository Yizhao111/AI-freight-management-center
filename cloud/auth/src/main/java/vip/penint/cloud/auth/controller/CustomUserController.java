package vip.penint.cloud.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.auth.entity.CustomUser;
import vip.penint.cloud.auth.service.ICustomUserService;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.PenintResponse;
import vip.penint.cloud.common.core.entity.QueryRequest;

import java.util.Arrays;

/**
 * authController
 *
 * @author Penint
 * @date 2023-07-17
 */
@RestController
@RequestMapping("/customUser")
public class CustomUserController {
    @Autowired
    private ICustomUserService customUserService;

    /**
     * 查询auth列表
     */
    @PreAuthorize("hasAuthority('auth:customUser:list')")
    @GetMapping("/list")
    public AjaxResult list(CustomUser customUser, QueryRequest queryRequest) {
        return AjaxResult.success(customUserService.selectCustomUserList(customUser, queryRequest));
    }

    @GetMapping("/listAll")
    public AjaxResult listAll() {
        return AjaxResult.success(customUserService.list());
    }

    @GetMapping("/queryUserId")
    public PenintResponse queryUserId(@RequestParam Integer userId) {
        return new PenintResponse().data(customUserService.getById(userId));
    }

    @GetMapping("/queryCount")
    public PenintResponse queryCount() {
        return new PenintResponse().data(customUserService.count());
    }

    @GetMapping("/findLastTenDaysVisitCount")
    public AjaxResult findLastTenDaysVisitCount() {
        return AjaxResult.success(customUserService.findLastTenDaysVisitCount());
    }



    /**
     * 获取auth详细信息
     */
    @PreAuthorize("hasAuthority('auth:customUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(customUserService.getById(id));
    }

    /**
     * 新增auth
     */
    @PreAuthorize("hasAuthority('auth:customUser:add')")
    @PostMapping
    public AjaxResult add(@RequestBody CustomUser customUser) {
        return AjaxResult.success(customUserService.save(customUser));
    }

    /**
     * 修改auth
     */
    @PreAuthorize("hasAuthority('auth:customUser:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody CustomUser customUser) {
        return AjaxResult.success(customUserService.updateById(customUser));
    }

    /**
     * 删除auth
     */
    @PreAuthorize("hasAuthority('auth:customUser:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return AjaxResult.success(customUserService.removeByIds(Arrays.asList(ids)));
    }

}
