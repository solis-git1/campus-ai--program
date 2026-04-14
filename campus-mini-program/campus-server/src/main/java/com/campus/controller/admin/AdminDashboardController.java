package com.campus.controller.admin;

import com.campus.result.R;
import com.campus.service.AdminDashboardService;
import com.campus.vo.admin.DashboardVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 管理端数据统计看板接口
 */
@RestController
@RequestMapping("/admin/dashboard")
@Slf4j
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    /**
     * 获取完整的数据看板
     */
    @GetMapping
    public R<DashboardVO> getDashboard() {
        log.info("获取管理端数据看板");
        DashboardVO vo = adminDashboardService.getDashboardData();
        return R.success(vo);
    }

    /**
     * 获取用户行为统计
     */
    @GetMapping("/behavior-stats")
    public R<Map<String, Object>> behaviorStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        log.info("获取用户行为统计");
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : LocalDateTime.now().minusDays(30);
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : LocalDateTime.now();
        Map<String, Object> stats = adminDashboardService.getUserBehaviorStats(start, end);
        return R.success(stats);
    }

    /**
     * 获取功能使用统计
     */
    @GetMapping("/feature-usage")
    public R<Map<String, Object>> featureUsage() {
        log.info("获取功能使用统计");
        Map<String, Object> stats = adminDashboardService.getFeatureUsageStats();
        return R.success(stats);
    }

    /**
     * 获取资源使用统计
     */
    @GetMapping("/resource-usage")
    public R<Map<String, Object>> resourceUsage() {
        log.info("获取资源使用统计");
        Map<String, Object> stats = adminDashboardService.getResourceUsageStats();
        return R.success(stats);
    }

    /**
     * 获取趋势数据
     */
    @GetMapping("/trend")
    public R<Map<String, Object>> trend(@RequestParam(defaultValue = "week") String period) {
        log.info("获取趋势数据，period:{}", period);
        Map<String, Object> trend = adminDashboardService.getTrendData(period);
        return R.success(trend);
    }
}
