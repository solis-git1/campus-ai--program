package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 校园活动实体类
 * 
 * <p>该类对应数据库中的活动表，用于存储校园内各类活动的详细信息，
 * 包括学术讲座、文体活动、社团活动等不同类型的校园活动。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class Activity {
    /**
     * 活动唯一标识ID，主键
     */
    private Long activityId;
    
    /**
     * 活动标题
     */
    private String title;
    
    /**
     * 活动类型（如：讲座、比赛、演出等）
     */
    private String type;
    
    /**
     * 活动举办地点
     */
    private String location;
    
    /**
     * 活动开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 活动主办方（如：学生会、社团等）
     */
    private String organizer;
    
    /**
     * 活动详细描述
     */
    private String description;
    
    /**
     * 活动联系人及联系方式
     */
    private String contact;
    
    /**
     * 活动最大参与人数限制
     */
    private Integer maxParticipants;
    
    /**
     * 当前已报名人数
     */
    private Integer currentParticipants;
    
    /**
     * 活动状态（未开始、进行中、已结束等）
     */
    private String status;
    
    /**
     * 活动创建时间
     */
    private LocalDateTime createTime;
}