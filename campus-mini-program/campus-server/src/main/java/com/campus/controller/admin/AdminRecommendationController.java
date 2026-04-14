package com.campus.controller.admin;

import com.campus.dto.admin.RecommendationConfigDTO;
import com.campus.entity.Recommendation;
import com.campus.result.R;
import com.campus.service.AdminRecommendationService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理端推荐管理接口
 */
@RestController
@RequestMapping("/admin/recommendation")
@Slf4j
@RequiredArgsConstructor
public class AdminRecommendationController {

    private final AdminRecommendationService adminRecommendationService;

    /**
     * 查询推荐记录
     */
    @GetMapping("/list")
    public R<PageInfo<Recommendation>> list(@RequestParam(required = false) String recType,
                                             @RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询推荐记录");
        PageInfo<Recommendation> pageInfo = adminRecommendationService.listRecommendations(recType, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 获取推荐配置列表
     */
    @GetMapping("/configs")
    public R<List<Map<String, Object>>> listConfigs() {
        log.info("获取推荐配置列表");
        List<Map<String, Object>> configs = adminRecommendationService.listRecommendationConfigs();
        return R.success(configs);
    }

    /**
     * 更新推荐配置
     */
    @PutMapping("/configs")
    public R updateConfig(@RequestBody RecommendationConfigDTO dto) {
        log.info("管理员更新推荐配置");
        adminRecommendationService.updateRecommendationConfig(dto);
        return R.success();
    }

    /**
     * 获取推荐效果统计
     */
    @GetMapping("/stats")
    public R<Map<String, Object>> stats() {
        log.info("获取推荐效果统计");
        Map<String, Object> stats = adminRecommendationService.getRecommendationStats();
        return R.success(stats);
    }

    /**
     * 刷新推荐
     */
    @PostMapping("/refresh")
    public R refresh() {
        log.info("手动刷新推荐");
        adminRecommendationService.refreshRecommendations();
        return R.success();
    }
}
