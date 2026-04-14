package com.campus.controller.user;

import com.campus.entity.Notification;
import com.campus.result.R;
import com.campus.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端系统通知相关接口
 */
@RestController
@RequestMapping("/user/notification")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 获取我的通知列表
     * @return 通知列表
     */
    @GetMapping("/my")
    public R<List<Notification>> getMy() {
        log.info("获取我的通知列表");
        List<Notification> list = notificationService.getMyNotification();
        return R.success(list);
    }

    /**
     * 标记通知为已读
     * @param notificationId 通知id
     * @return 成功结果
     */
    @PostMapping("/read/{notificationId}")
    public R markRead(@PathVariable Long notificationId) {
        log.info("标记通知已读，notificationId:{}", notificationId);
        notificationService.markRead(notificationId);
        return R.success();
    }
}