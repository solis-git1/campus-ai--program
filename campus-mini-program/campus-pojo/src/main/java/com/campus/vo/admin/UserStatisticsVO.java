package com.campus.vo.admin;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户统计信息视图对象
 * 
 * <p>该类用于封装用户相关的统计信息，主要用于管理员后台的数据分析，
 * 提供用户数量、活跃度、角色分布等关键指标的汇总数据。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class UserStatisticsVO {
    /**
     * 总用户数量
     */
    private Long totalUsers;
    
    /**
     * 活跃用户数量（指定时间内登录过的用户）
     */
    private Long activeUsers;
    
    /**
     * 今日新增用户数量
     */
    private Long newUsersToday;
    
    /**
     * 本周新增用户数量
     */
    private Long newUsersThisWeek;
    
    /**
     * 学生用户数量
     */
    private Long studentCount;
    
    /**
     * 管理员用户数量
     */
    private Long adminCount;
}
