package com.campus.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 课程创建/更新DTO
 */
@Data
public class CourseDTO {
    private Long courseId;
    @NotBlank(message = "课程名称不能为空")
    private String courseName;
    private String teacher;
    @NotNull(message = "教室ID不能为空")
    private Long classroomId;
    private Integer weekday;
    private LocalTime startTime;
    private LocalTime endTime;
    private String type;
}
