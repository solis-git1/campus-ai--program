package com.campus.service.impl;

import com.campus.context.BaseContext;
import com.campus.entity.Notification;
import com.campus.exception.BaseException;
import com.campus.mapper.NotificationMapper;
import com.campus.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统通知业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    /**
     * 获取当前用户的通知列表
     */
    @Override
    public List<Notification> getMyNotification() {
        Long userId = BaseContext.getCurrentId();
        return notificationMapper.listByUserId(userId);
    }

    /**
     * 标记通知为已读
     */
    @Override
    public void markRead(Long notificationId) {
        Long userId = BaseContext.getCurrentId();
        int rows = notificationMapper.markRead(notificationId, userId);
        if (rows == 0) {
            throw new BaseException("通知不存在或无权操作");
        }
    }
}