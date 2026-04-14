package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动报名记录实体类
 * 
 * <p>该类对应数据库中的活动报名表，用于记录用户对校园活动的报名信息，
 * 包括报名时间、报名状态等关键信息。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class ActivityRegistration {
    /**
     * 报名记录唯一标识ID，主键
     */
    private Long registrationId;
    
    /**
     * 用户ID，关联用户表
     */
    private Long userId;
    
    /**
     * 活动ID，关联活动表
     */
    private Long activityId;
    
    /**
     * 用户报名时间
     */
    private LocalDateTime registerTime;
    
    /**
     * 报名状态（如：已报名、已取消、已参加等）
     */
    private String status;
}