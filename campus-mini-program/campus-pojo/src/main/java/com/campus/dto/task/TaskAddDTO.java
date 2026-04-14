package com.campus.dto.task;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 待办添加DTO
 */
@Data
public class TaskAddDTO {
    @NotBlank(message = "任务标题不能为空")
    private String title;
    private String content;
    private String category;
    private String priority;
    private LocalDateTime deadline;
    private LocalDateTime remindTime;
}