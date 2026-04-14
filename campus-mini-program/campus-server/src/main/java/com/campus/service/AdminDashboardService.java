package com.campus.service;

import com.campus.vo.admin.DashboardVO;
import java.util.Map;

/**
 * 管理端数据统计看板业务接口
 */
public interface AdminDashboardService {

    /**
     * 获取完整的数据看板信息
     * @return 看板数据
     */
    DashboardVO getDashboardData();

    /**
     * 获取用户行为统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 行为统计
     */
    Map<String, Object> getUserBehaviorStats(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    /**
     * 获取功能使用统计
     * @return 功能统计
     */
    Map<String, Object> getFeatureUsageStats();

    /**
     * 获取活动/课程/教室的使用数据统计
     * @return 使用数据统计
     */
    Map<String, Object> getResourceUsageStats();

    /**
     * 获取趋势数据（按天/周/月）
     * @param period 周期：day/week/month
     * @return 趋势数据
     */
    Map<String, Object> getTrendData(String period);
}
