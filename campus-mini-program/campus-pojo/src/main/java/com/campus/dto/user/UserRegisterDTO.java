package com.campus.dto.user;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户注册DTO
 */
@Data
public class UserRegisterDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String nickname;
    private String phone;
}