package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * AI聊天会话实体类
 * 
 * <p>该类对应数据库中的AI聊天会话表，用于存储用户与AI助手的对话会话信息，
 * 支持多轮对话的上下文管理和会话历史记录。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class AiChatSession {
    /**
     * 会话唯一标识ID，主键
     */
    private Long sessionId;
    
    /**
     * 用户ID，关联用户表
     */
    private Long userId;
    
    /**
     * 会话标题，用于快速识别对话内容
     */
    private String title;
    
    /**
     * 会话创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 会话最后活跃时间，用于清理过期会话
     */
    private LocalDateTime lastActiveTime;
    
    /**
     * 逻辑删除标识：0-未删除，1-已删除
     */
    private Integer isDeleted;
}