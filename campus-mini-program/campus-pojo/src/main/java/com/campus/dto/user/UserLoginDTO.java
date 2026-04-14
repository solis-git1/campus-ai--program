package com.campus.dto.user;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录DTO
 */
@Data
public class UserLoginDTO {
    @NotBlank(message = "code不能为空")
    private String code; //微信登录code
}