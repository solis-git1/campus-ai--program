package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * AI对话消息实体类
 * 
 * <p>该类对应数据库中的AI消息表，用于存储用户与AI助手之间的对话消息记录，
 * 包括用户提问和AI回复的完整对话历史。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class AiMessage {
    /**
     * 消息唯一标识ID，主键
     */
    private Long messageId;
    
    /**
     * 会话ID，关联AI聊天会话表
     */
    private Long sessionId;
    
    /**
     * 用户ID，关联用户表
     */
    private Long userId;
    
    /**
     * 消息角色（user-用户，assistant-AI助手）
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 信息来源（如：web、app、api等）
     */
    private String source;
    
    /**
     * 消息消耗的Token数量，用于成本计算
     */
    private Integer tokens;
    
    /**
     * 消息发送时间
     */
    private LocalDateTime createTime;
}