package com.campus.dto.admin;

import lombok.Data;

/**
 * 用户状态更新DTO
 */
@Data
public class UserStatusDTO {
    private Long userId;
    private Integer status;
}
