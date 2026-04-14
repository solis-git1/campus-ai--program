package com.campus.dto.admin;

import lombok.Data;

/**
 * 报名状态更新DTO
 */
@Data
public class RegistrationStatusDTO {
    private Long registrationId;
    private String status;
}
