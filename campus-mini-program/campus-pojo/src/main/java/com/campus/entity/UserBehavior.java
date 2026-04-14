package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户行为记录
 */
@Data
public class UserBehavior {
    private Long behaviorId; //行为ID
    private Long userId; //用户ID
    private String behaviorType; //行为类型
    private Long targetId; //目标ID
    private String targetType; //目标类型
    private String content; //行为内容
    private LocalDateTime createTime; //行为时间
}