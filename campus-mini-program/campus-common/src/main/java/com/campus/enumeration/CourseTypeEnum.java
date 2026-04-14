package com.campus.enumeration;

import lombok.Getter;

/**
 * 课程类型枚举
 */
@Getter
public enum CourseTypeEnum {
    /**
     * 必修课
     */
    REQUIRED("必修", "required"),
    /**
     * 选修课
     */
    OPTIONAL("选修", "optional");

    private final String description;
    private final String code;

    CourseTypeEnum(String description, String code) {
        this.description = description;
        this.code = code;
    }
}