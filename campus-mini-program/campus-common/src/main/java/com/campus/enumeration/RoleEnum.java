package com.campus.enumeration;

import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum RoleEnum {
    /**
     * 学生
     */
    STUDENT("student", "学生"),
    /**
     * 管理员
     */
    ADMIN("admin", "管理员");

    private final String code;
    private final String description;

    RoleEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}