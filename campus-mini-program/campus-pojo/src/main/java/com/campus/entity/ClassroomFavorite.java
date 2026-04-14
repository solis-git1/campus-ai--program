package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 教室收藏实体类
 * 
 * <p>该类对应数据库中的教室收藏表，用于记录用户对常用教室的收藏关系，
 * 方便用户快速查找和访问常用教室。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class ClassroomFavorite {
    /**
     * 收藏记录唯一标识ID，主键
     */
    private Long favoriteId;
    
    /**
     * 用户ID，关联用户表
     */
    private Long userId;
    
    /**
     * 教室ID，关联教室表
     */
    private Long classroomId;
    
    /**
     * 收藏创建时间
     */
    private LocalDateTime createTime;
}
