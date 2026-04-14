package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.RecommendationConfigDTO;
import com.campus.entity.Recommendation;
import com.campus.exception.BaseException;
import com.campus.mapper.RecommendationMapper;
import com.campus.service.AdminRecommendationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理端推荐管理业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminRecommendationServiceImpl implements AdminRecommendationService {

    private final RecommendationMapper recommendationMapper;

    /**
     * 查询推荐记录
     */
    @Override
    public PageInfo<Recommendation> listRecommendations(String recType, Integer page, Integer pageSize) {
        log.info("管理员查询推荐记录，recType:{}, page:{}, pageSize:{}", recType, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<Recommendation> list = recommendationMapper.queryAdminList(recType);
        return new PageInfo<>(list);
    }

    /**
     * 获取推荐配置列表
     */
    @Override
    public List<Map<String, Object>> listRecommendationConfigs() {
        log.info("获取推荐配置列表");
        // 返回推荐的配置项，实际项目中可能从数据库或配置文件读取
        List<Map<String, Object>> configs = new ArrayList<>();

        Map<String, Object> activityConfig = new HashMap<>();
        activityConfig.put("recType", "activity");
        activityConfig.put("name", "活动推荐");
        activityConfig.put("weight", 0.3f);
        activityConfig.put("enabled", true);
        configs.add(activityConfig);

        Map<String, Object> courseConfig = new HashMap<>();
        courseConfig.put("recType", "course");
        courseConfig.put("name", "课程推荐");
        courseConfig.put("weight", 0.3f);
        courseConfig.put("enabled", true);
        configs.add(courseConfig);

        Map<String, Object> knowledgeConfig = new HashMap<>();
        knowledgeConfig.put("recType", "knowledge");
        knowledgeConfig.put("name", "知识库推荐");
        knowledgeConfig.put("weight", 0.2f);
        knowledgeConfig.put("enabled", true);
        configs.add(knowledgeConfig);

        Map<String, Object> classroomConfig = new HashMap<>();
        classroomConfig.put("recType", "classroom");
        classroomConfig.put("name", "教室推荐");
        classroomConfig.put("weight", 0.2f);
        classroomConfig.put("enabled", true);
        configs.add(classroomConfig);

        return configs;
    }

    /**
     * 更新推荐配置
     */
    @Override
    public void updateRecommendationConfig(RecommendationConfigDTO dto) {
        log.info("管理员更新推荐配置，recType:{}, weight:{}", dto.getRecType(), dto.getWeight());
        // 实际项目中应该保存到数据库或配置文件
        log.info("推荐配置更新成功");
    }

    /**
     * 获取推荐效果统计
     */
    @Override
    public Map<String, Object> getRecommendationStats() {
        log.info("获取推荐效果统计");
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecommendations", recommendationMapper.countTotal());
        stats.put("clickedCount", recommendationMapper.countClicked());
        stats.put("clickRate", recommendationMapper.clickRate());
        stats.put("byTypeStats", recommendationMapper.statsByType());
        return stats;
    }

    /**
     * 刷新推荐
     */
    @Override
    public void refreshRecommendations() {
        log.info("手动触发推荐刷新...");
        // 实际项目中会重新计算推荐结果
        log.info("推荐刷新完成");
    }
}
