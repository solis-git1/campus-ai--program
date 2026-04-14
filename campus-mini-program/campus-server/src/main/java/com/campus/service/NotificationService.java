package com.campus.service;

import com.campus.entity.Notification;

import java.util.List;

/**
 * 系统通知业务接口
 */
public interface NotificationService {

    /**
     * 获取当前用户的通知列表
     * @return 通知列表
     */
    List<Notification> getMyNotification();

    /**
     * 标记通知为已读
     * @param notificationId 通知id
     */
    void markRead(Long notificationId);
}