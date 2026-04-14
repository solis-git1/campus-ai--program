package com.campus.mapper;

import com.campus.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {

    List<Notification> listByUserId(Long userId);

    void insert(Notification notification);

    int markRead(Long notificationId, Long userId);

    Notification getById(@Param("notificationId") Long notificationId);

    List<Notification> queryAdminList(@Param("type") String type);

    void update(Notification notification);

    void deleteById(@Param("notificationId") Long notificationId);

    Long countByTemplateId(@Param("templateId") Long templateId);

    Long countReadByTemplateId(@Param("templateId") Long templateId);
}
