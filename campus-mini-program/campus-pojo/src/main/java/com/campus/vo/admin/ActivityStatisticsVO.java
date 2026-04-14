package com.campus.vo.admin;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动统计信息视图对象
 * 
 * <p>该类用于封装活动相关的统计信息，主要用于管理员后台的数据分析，
 * 提供活动数量、报名情况等关键指标的汇总数据。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class ActivityStatisticsVO {
    /**
     * 活动总数
     */
    private Long totalActivities;
    
    /**
     * 进行中的活动数量
     */
    private Long ongoingActivities;
    
    /**
     * 已结束的活动数量
     */
    private Long completedActivities;
    
    /**
     * 总报名人数（所有活动的报名人数总和）
     */
    private Long totalRegistrations;
    
    /**
     * 平均报名率（报名人数/活动容量）
     */
    private Double avgRegistrationRate;
    
    /**
     * 统计开始时间
     */
    private LocalDateTime startDate;
    
    /**
     * 统计结束时间
     */
    private LocalDateTime endDate;
}
