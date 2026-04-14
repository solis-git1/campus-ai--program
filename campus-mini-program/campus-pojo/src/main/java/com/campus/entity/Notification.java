package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统通知
 */
@Data
public class Notification {
    private Long notificationId; //通知ID
    private Long userId; //用户ID
    private String title; //通知标题
    private String content; //通知内容
    private String type; //通知类型
    private Integer isRead; //是否已读
    private LocalDateTime createTime; //创建时间
}