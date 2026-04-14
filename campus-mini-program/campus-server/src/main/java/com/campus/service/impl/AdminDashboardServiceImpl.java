package com.campus.service.impl;

import com.campus.service.AdminAiService;
import com.campus.service.AdminActivityService;
import com.campus.service.AdminClassroomService;
import com.campus.service.AdminCourseService;
import com.campus.service.AdminDashboardService;
import com.campus.service.AdminKnowledgeService;
import com.campus.service.AdminNotificationService;
import com.campus.service.AdminRecommendationService;
import com.campus.service.AdminUserService;
import com.campus.mapper.UserBehaviorMapper;
import com.campus.vo.admin.ActivityStatisticsVO;
import com.campus.vo.admin.DashboardVO;
import com.campus.vo.admin.UserStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理端数据统计看板业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final AdminUserService adminUserService;
    private final AdminActivityService adminActivityService;
    private final AdminClassroomService adminClassroomService;
    private final AdminCourseService adminCourseService;
    private final AdminKnowledgeService adminKnowledgeService;
    private final AdminNotificationService adminNotificationService;
    private final AdminAiService adminAiService;
    private final AdminRecommendationService adminRecommendationService;
    private final UserBehaviorMapper userBehaviorMapper;

    /**
     * 获取完整的数据看板
     */
    @Override
    public DashboardVO getDashboardData() {
        log.info("获取管理端数据看板");
        DashboardVO vo = new DashboardVO();

        // 用户统计
        UserStatisticsVO userStats = adminUserService.getUserStatistics();
        vo.setUserStats(userStats);

        // 活动统计
        ActivityStatisticsVO activityStats = adminActivityService.getActivityStatistics();
        vo.setActivityStats(activityStats);

        // 教室统计
        Map<String, Object> classroomStats = new HashMap<>();
        classroomStats.put("totalClassrooms", 0); // 需要从mapper获取
        classroomStats.put("availableCount", 0);
        classroomStats.put("occupiedCount", 0);
        vo.setClassroomStats(classroomStats);

        // 课程统计
        Map<String, Object> courseStats = new HashMap<>();
        courseStats.put("totalCourses", 0);
        courseStats.put("byType", List.of());
        vo.setCourseStats(courseStats);

        // 知识库统计
        Map<String, Object> knowledgeStats = new HashMap<>();
        knowledgeStats.put("totalDocuments", 0);
        knowledgeStats.put("totalChunks", 0);
        vo.setKnowledgeStats(knowledgeStats);

        // AI统计
        Map<String, Object> aiStats = adminAiService.getAiStatistics();
        vo.setAiStats(aiStats);

        // 行为统计
        Map<String, Object> behaviorStats = getUserBehaviorStats(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
        );
        vo.setBehaviorStats(behaviorStats);

        return vo;
    }

    /**
     * 获取用户行为统计
     */
    @Override
    public Map<String, Object> getUserBehaviorStats(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("获取用户行为统计，startDate:{}, endDate:{}", startDate, endDate);
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalBehaviors", userBehaviorMapper.countByDateRange(startDate, endDate));
        stats.put("byType", userBehaviorMapper.statsByType(startDate, endDate));
        stats.put("activeUsers", userBehaviorMapper.countActiveUsers(startDate, endDate));
        stats.put("topFeatures", userBehaviorMapper.topFeatures(10));

        return stats;
    }

    /**
     * 获取功能使用统计
     */
    @Override
    public Map<String, Object> getFeatureUsageStats() {
        log.info("获取功能使用统计");
        Map<String, Object> stats = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusWeeks(1);

        stats.put("activityUsage", userBehaviorMapper.countByTargetTypeAndPeriod("activity", weekAgo, now));
        stats.put("courseUsage", userBehaviorMapper.countByTargetTypeAndPeriod("course", weekAgo, now));
        stats.put("classroomUsage", userBehaviorMapper.countByTargetTypeAndPeriod("classroom", weekAgo, now));
        stats.put("knowledgeUsage", userBehaviorMapper.countByTargetTypeAndPeriod("knowledge", weekAgo, now));
        stats.put("aiUsage", userBehaviorMapper.countByTargetTypeAndPeriod("ai_chat", weekAgo, now));

        return stats;
    }

    /**
     * 获取资源使用统计
     */
    @Override
    public Map<String, Object> getResourceUsageStats() {
        log.info("获取资源使用统计");
        Map<String, Object> stats = new HashMap<>();

        // 活动相关
        ActivityStatisticsVO activityStats = adminActivityService.getActivityStatistics();
        stats.put("activityTotal", activityStats.getTotalActivities());
        stats.put("activityRegistrations", activityStats.getTotalRegistrations());

        // 课程相关（需要从mapper获取实际数据）
        stats.put("courseTotal", 0);
        stats.put("courseByType", List.of());

        // 教室相关
        stats.put("classroomTotal", 0);
        stats.put("classroomUtilization", 0.0);

        return stats;
    }

    /**
     * 获取趋势数据
     */
    @Override
    public Map<String, Object> getTrendData(String period) {
        log.info("获取趋势数据，period:{}", period);
        Map<String, Object> trend = new HashMap<>();

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate;

        switch (period != null ? period : "week") {
            case "day":
                startDate = endDate.minusDays(1);
                break;
            case "month":
                startDate = endDate.minusMonths(1);
                break;
            default: // week
                startDate = endDate.minusWeeks(1);
                break;
        }

        trend.put("newUsersTrend", userBehaviorMapper.newUserTrend(startDate, endDate, period));
        trend.put("behaviorTrend", userBehaviorMapper.behaviorTrend(startDate, endDate, period));
        trend.put("loginTrend", userBehaviorMapper.loginTrend(startDate, endDate, period));

        return trend;
    }
}
