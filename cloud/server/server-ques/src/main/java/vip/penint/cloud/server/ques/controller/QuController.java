package vip.penint.cloud.server.ques.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.server.ques.entity.Qu;
import vip.penint.cloud.server.ques.service.IQuService;

import java.util.Arrays;

/**
 * <p>
 * 题目表 前端控制器
 * </p>
 *
 * @author penint
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/qu")
public class QuController {


    @Autowired
    private IQuService tQuService;


    /**
     * 查询题目列表
     */
    @GetMapping("/list")
    public AjaxResult list(Qu qu, QueryRequest queryRequest) {
        return AjaxResult.success(tQuService.selectTQuList(qu, queryRequest));
    }

    /**
     * 获取题目详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(tQuService.getInfoById(id));
    }

    /**
     * 新增/修改题目
     */
    @PostMapping
    public AjaxResult add(@RequestBody Qu qu) throws Exception {
        tQuService.saveQu(qu);
        return AjaxResult.success();
    }

    /**
     * 删除题目
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        tQuService.delByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }


}
