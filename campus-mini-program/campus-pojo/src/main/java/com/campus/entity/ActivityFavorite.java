package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动收藏实体类
 * 
 * <p>该类对应数据库中的活动收藏表，用于记录用户对校园活动的收藏关系，
 * 实现用户个性化推荐和快速访问功能。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class ActivityFavorite {
    /**
     * 收藏记录唯一标识ID，主键
     */
    private Long favoriteId;
    
    /**
     * 用户ID，关联用户表
     */
    private Long userId;
    
    /**
     * 活动ID，关联活动表
     */
    private Long activityId;
    
    /**
     * 收藏创建时间
     */
    private LocalDateTime createTime;
}
