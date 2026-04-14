package com.campus.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 通知创建/更新DTO
 */
@Data
public class NotificationDTO {
    private Long notificationId;
    @NotBlank(message = "通知标题不能为空")
    private String title;
    @NotBlank(message = "通知内容不能为空")
    private String content;
    private String type;
}
