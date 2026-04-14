package com.campus.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 活动创建/更新DTO
 */
@Data
public class ActivityDTO {
    private Long activityId;
    @NotBlank(message = "活动标题不能为空")
    private String title;
    private String type;
    private String location;
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    private String organizer;
    private String description;
    private String contact;
    private Integer maxParticipants;
    private String status;
}
