package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 待办事项实体类
 * 
 * <p>该类对应数据库中的待办事项表，用于存储用户的个人任务和待办事项，
 * 支持任务管理、提醒、归档等功能。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class Task {
    /**
     * 任务唯一标识ID，主键
     */
    private Long taskId;
    
    /**
     * 用户ID，关联用户表
     */
    private Long userId;
    
    /**
     * 任务标题
     */
    private String title;
    
    /**
     * 任务详细内容
     */
    private String content;
    
    /**
     * 任务分类（如：学习、工作、生活等）
     */
    private String category;
    
    /**
     * 任务优先级（如：高、中、低）
     */
    private String priority;
    
    /**
     * 任务截止时间
     */
    private LocalDateTime deadline;
    
    /**
     * 任务状态（未完成、已完成等）
     */
    private String status;
    
    /**
     * 是否归档：0-未归档，1-已归档
     */
    private Integer isArchived;
    
    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 任务完成时间
     */
    private LocalDateTime completeTime;
}