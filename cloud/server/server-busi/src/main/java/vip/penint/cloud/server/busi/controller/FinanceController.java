package vip.penint.cloud.server.busi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.server.busi.entity.Finance;
import vip.penint.cloud.server.busi.service.IFinanceService;

/**
 * @author Hxx
 * @Description:财务表 控制器
 * @date 2023-07-19
 */
@RestController
@RequestMapping("/finance")
public class FinanceController {
    @Autowired
    private IFinanceService financeService;

    /**
     * 查询财务表列表
     */
    @PreAuthorize("hasAuthority('busi:finance:list')")
    @GetMapping("/list")
    public AjaxResult list(Finance entity, QueryRequest queryRequest) {
        return AjaxResult.success(financeService.selectFinanceList(entity, queryRequest));
    }


}
