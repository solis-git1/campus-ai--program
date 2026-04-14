package com.campus.vo.admin;

import lombok.Data;
import java.util.Map;

/**
 * 数据看板统计视图对象
 * 
 * <p>该类用于封装管理员后台数据看板的综合统计信息，
 * 整合了用户、活动、教室、课程、知识库、AI助手、用户行为等多个维度的统计数据。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class DashboardVO {
    /**
     * 用户统计信息
     */
    private UserStatisticsVO userStats;
    
    /**
     * 活动统计信息
     */
    private ActivityStatisticsVO activityStats;
    
    /**
     * 教室使用情况统计
     */
    private Map<String, Object> classroomStats;
    
    /**
     * 课程相关统计信息
     */
    private Map<String, Object> courseStats;
    
    /**
     * 知识库使用统计
     */
    private Map<String, Object> knowledgeStats;
    
    /**
     * AI助手使用统计
     */
    private Map<String, Object> aiStats;
    
    /**
     * 用户行为分析统计
     */
    private Map<String, Object> behaviorStats;
}
