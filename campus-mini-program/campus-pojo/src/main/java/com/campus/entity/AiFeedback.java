package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * AI反馈实体类
 * 
 * <p>该类对应数据库中的AI反馈表，用于收集用户对AI助手回答的评价和反馈，
 * 帮助改进AI模型的质量和用户体验。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class AiFeedback {
    /**
     * 反馈记录唯一标识ID，主键
     */
    private Long feedbackId;
    
    /**
     * AI消息ID，关联AI消息表
     */
    private Long messageId;
    
    /**
     * 用户ID，关联用户表
     */
    private Long userId;
    
    /**
     * 用户评分（1-5星）
     */
    private Integer rating;
    
    /**
     * 用户反馈评论
     */
    private String comment;
    
    /**
     * 反馈处理状态（0-待处理，1-已处理）
     */
    private Integer status;
    
    /**
     * 管理员回复内容
     */
    private String reply;
    
    /**
     * 反馈创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 反馈更新时间
     */
    private LocalDateTime updateTime;
}
