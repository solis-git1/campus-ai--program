package com.campus.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminLoginDTO {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度应为2-50个字符")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 1, max = 100, message = "密码长度不合法")
    private String password;
}
