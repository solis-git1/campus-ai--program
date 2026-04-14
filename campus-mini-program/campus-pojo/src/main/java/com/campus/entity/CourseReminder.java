package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 课程提醒
 */
@Data
public class CourseReminder {
    private Long reminderId; //提醒ID
    private Long userId; //用户ID
    private Long courseId; //课程ID
    private LocalDateTime remindTime; //提醒时间
    private String repeatType; //重复类型
}