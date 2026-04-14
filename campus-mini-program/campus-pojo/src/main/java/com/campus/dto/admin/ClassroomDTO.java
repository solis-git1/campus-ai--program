package com.campus.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 教室创建/更新DTO
 */
@Data
public class ClassroomDTO {
    private Long classroomId;
    @NotNull(message = "教学楼ID不能为空")
    private Long buildingId;
    @NotBlank(message = "教室编号不能为空")
    private String roomNumber;
    private Integer floor;
    private Integer capacity;
    private Integer hasMedia;
    private String status;
}
