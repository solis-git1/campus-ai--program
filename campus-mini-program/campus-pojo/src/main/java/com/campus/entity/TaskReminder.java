package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 待办提醒
 */
@Data
public class TaskReminder {
    private Long reminderId; //提醒ID
    private Long taskId; //任务ID
    private LocalDateTime remindTime; //提醒时间
    private Integer sendCount; //发送次数
    private LocalDateTime lastSendTime; //最后发送时间
}