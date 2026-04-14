package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.NotificationDTO;
import com.campus.dto.admin.NotificationPushDTO;
import com.campus.entity.Notification;
import com.campus.entity.User;
import com.campus.exception.BaseException;
import com.campus.mapper.NotificationMapper;
import com.campus.mapper.UserMapper;
import com.campus.service.AdminNotificationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理端通知管理业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminNotificationServiceImpl implements AdminNotificationService {

    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;

    /**
     * 分页查询通知列表
     */
    @Override
    public PageInfo<Notification> listNotifications(String type, Integer page, Integer pageSize) {
        log.info("管理员查询通知列表，type:{}, page:{}, pageSize:{}", type, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<Notification> list = notificationMapper.queryAdminList(type);
        return new PageInfo<>(list);
    }

    /**
     * 查询通知详情
     */
    @Override
    public Notification getNotificationDetail(Long notificationId) {
        log.info("管理员查看通知详情，notificationId:{}", notificationId);
        Notification notification = notificationMapper.getById(notificationId);
        if (notification == null) {
            throw new BaseException("通知不存在");
        }
        return notification;
    }

    /**
     * 创建通知
     */
    @Override
    public Long createNotification(NotificationDTO dto) {
        log.info("管理员创建通知：{}", dto.getTitle());
        Notification notification = new Notification();
        notification.setTitle(dto.getTitle());
        notification.setContent(dto.getContent());
        notification.setType(dto.getType() != null ? dto.getType() : "system");
        notification.setIsRead(0);
        notification.setCreateTime(LocalDateTime.now());

        notificationMapper.insert(notification);
        log.info("通知创建成功，notificationId:{}", notification.getNotificationId());
        return notification.getNotificationId();
    }

    /**
     * 更新通知
     */
    @Override
    public void updateNotification(NotificationDTO dto) {
        log.info("管理员更新通知，notificationId:{}", dto.getNotificationId());
        Notification exist = notificationMapper.getById(dto.getNotificationId());
        if (exist == null) {
            throw new BaseException("通知不存在");
        }

        Notification notification = new Notification();
        notification.setNotificationId(dto.getNotificationId());
        notification.setTitle(dto.getTitle());
        notification.setContent(dto.getContent());
        if (dto.getType() != null) {
            notification.setType(dto.getType());
        }

        notificationMapper.update(notification);
        log.info("通知更新成功");
    }

    /**
     * 删除通知
     */
    @Override
    public void deleteNotification(Long notificationId) {
        log.info("管理员删除通知，notificationId:{}", notificationId);
        Notification exist = notificationMapper.getById(notificationId);
        if (exist == null) {
            throw new BaseException("通知不存在");
        }
        notificationMapper.deleteById(notificationId);
        log.info("通知删除成功");
    }

    /**
     * 推送通知给用户
     */
    @Override
    @Transactional
    public void pushNotification(NotificationPushDTO dto) {
        log.info("管理员推送通知，notificationId:{}, pushAll:{}", dto.getNotificationId(), dto.getPushAll());
        Notification notification = notificationMapper.getById(dto.getNotificationId());
        if (notification == null) {
            throw new BaseException("通知不存在");
        }

        List<User> users;
        if (dto.getPushAll() != null && dto.getPushAll() == 1) {
            // 推送给所有用户
            users = userMapper.listAll();
        } else if (dto.getUserIds() != null && !dto.getUserIds().isEmpty()) {
            // 推送给指定用户
            users = userMapper.listByIds(dto.getUserIds());
        } else {
            throw new BaseException("请选择推送目标");
        }

        int count = 0;
        for (User user : users) {
            Notification userNotif = new Notification();
            userNotif.setUserId(user.getUserId());
            userNotif.setTitle(notification.getTitle());
            userNotif.setContent(notification.getContent());
            userNotif.setType(notification.getType());
            userNotif.setIsRead(0);
            userNotif.setCreateTime(LocalDateTime.now());
            notificationMapper.insert(userNotif);
            count++;
        }

        log.info("通知推送完成，共推送给{}个用户", count);
    }

    /**
     * 获取通知统计
     */
    @Override
    public Map<String, Object> getNotificationStats(Long notificationId) {
        Map<String, Object> stats = new HashMap<>();
        Long total = notificationMapper.countByTemplateId(notificationId);
        Long readCount = notificationMapper.countReadByTemplateId(notificationId);
        stats.put("total", total);
        stats.put("readCount", readCount);
        stats.put("unreadCount", total - readCount);
        return stats;
    }
}
