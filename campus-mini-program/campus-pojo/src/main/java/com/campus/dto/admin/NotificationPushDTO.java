package com.campus.dto.admin;

import lombok.Data;

/**
 * 推送通知DTO
 */
@Data
public class NotificationPushDTO {
    private Long notificationId;
    private Integer pushAll; //是否推送给所有用户：1-是，0-否
    private java.util.List<Long> userIds; //指定用户ID列表（pushAll=0时使用）
}
