package com.campus.controller.admin;

import com.campus.dto.admin.NotificationDTO;
import com.campus.dto.admin.NotificationPushDTO;
import com.campus.entity.Notification;
import com.campus.result.R;
import com.campus.service.AdminNotificationService;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端通知管理接口
 */
@RestController
@RequestMapping("/admin/notification")
@Slf4j
@RequiredArgsConstructor
public class AdminNotificationController {

    private final AdminNotificationService adminNotificationService;

    /**
     * 分页查询通知列表
     */
    @GetMapping("/list")
    public R<PageInfo<Notification>> list(@RequestParam(required = false) String type,
                                           @RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询通知列表");
        PageInfo<Notification> pageInfo = adminNotificationService.listNotifications(type, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 查询通知详情
     */
    @GetMapping("/{notificationId}")
    public R<Notification> detail(@PathVariable Long notificationId) {
        log.info("管理员查看通知详情，notificationId:{}", notificationId);
        Notification notification = adminNotificationService.getNotificationDetail(notificationId);
        return R.success(notification);
    }

    /**
     * 创建通知
     */
    @PostMapping
    public R<Long> create(@RequestBody @Valid NotificationDTO dto) {
        log.info("管理员创建通知");
        Long notificationId = adminNotificationService.createNotification(dto);
        return R.success(notificationId);
    }

    /**
     * 更新通知
     */
    @PutMapping
    public R update(@RequestBody @Valid NotificationDTO dto) {
        log.info("管理员更新通知");
        adminNotificationService.updateNotification(dto);
        return R.success();
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/{notificationId}")
    public R delete(@PathVariable Long notificationId) {
        log.info("管理员删除通知，notificationId:{}", notificationId);
        adminNotificationService.deleteNotification(notificationId);
        return R.success();
    }

    /**
     * 推送通知
     */
    @PostMapping("/push")
    public R push(@RequestBody NotificationPushDTO dto) {
        log.info("管理员推送通知");
        adminNotificationService.pushNotification(dto);
        return R.success();
    }

    /**
     * 获取通知统计
     */
    @GetMapping("/{notificationId}/stats")
    public R<Map<String, Object>> stats(@PathVariable Long notificationId) {
        log.info("获取通知统计");
        Map<String, Object> stats = adminNotificationService.getNotificationStats(notificationId);
        return R.success(stats);
    }
}
