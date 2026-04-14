package com.campus.service;

import com.campus.dto.admin.NotificationDTO;
import com.campus.dto.admin.NotificationPushDTO;
import com.campus.entity.Notification;
import com.github.pagehelper.PageInfo;

/**
 * 管理端通知管理业务接口
 */
public interface AdminNotificationService {

    /**
     * 分页查询通知列表
     * @param type 类型
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    PageInfo<Notification> listNotifications(String type, Integer page, Integer pageSize);

    /**
     * 根据ID查询通知详情
     * @param notificationId 通知ID
     * @return 通知信息
     */
    Notification getNotificationDetail(Long notificationId);

    /**
     * 创建通知
     * @param dto 通知参数
     * @return 通知ID
     */
    Long createNotification(NotificationDTO dto);

    /**
     * 更新通知
     * @param dto 通知参数
     */
    void updateNotification(NotificationDTO dto);

    /**
     * 删除通知
     * @param notificationId 通知ID
     */
    void deleteNotification(Long notificationId);

    /**
     * 推送通知给用户
     * @param dto 推送参数
     */
    void pushNotification(NotificationPushDTO dto);

    /**
     * 获取通知统计（已读/未读数量）
     * @param notificationId 通知ID
     * @return 统计数据
     */
    java.util.Map<String, Object> getNotificationStats(Long notificationId);
}
