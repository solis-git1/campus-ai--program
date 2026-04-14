package com.campus.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户查询条件DTO
 */
@Data
public class UserQueryDTO {
    private String keyword; //关键词（用户名/昵称/手机号）
    private String role; //角色筛选
    private Integer status; //状态筛选
}
