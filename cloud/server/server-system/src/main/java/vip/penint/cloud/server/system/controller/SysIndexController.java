package vip.penint.cloud.server.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminApi/index")
public class SysIndexController {


//    @GetMapping
//    public AjaxResult index(HttpServletRequest request) {
//        String username = PenintUtil.getCurrentUsername();
//        Map<String, Object> data = new HashMap<>(5);
//        // 获取系统访问记录
//        Long totalVisitCount = loginLogService.findTotalVisitCount();
//        data.put("totalVisitCount", totalVisitCount);
//        Long todayVisitCount = loginLogService.findTodayVisitCount();
//        data.put("todayVisitCount", todayVisitCount);
//        Long todayIp = loginLogService.findTodayIp();
//        data.put("todayIp", todayIp);
//        // 获取近期系统访问记录
//        List<Map<String, Object>> lastTenVisitCount = loginLogService.findLastTenDaysVisitCount(null);
//        data.put("lastTenVisitCount", lastTenVisitCount);
//
//        List<Map<String, Object>> lastTenUserVisitCount = loginLogService.findLastTenDaysVisitCount(username);
//        data.put("lastTenUserVisitCount", lastTenUserVisitCount);
//        return AjaxResult.success(data);
//    }
}