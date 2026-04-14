package com.campus.enumeration;

import lombok.Getter;

/**
 * 待办任务状态枚举
 */
@Getter
public enum TaskStatusEnum {
    /**
     * 未完成
     */
    UNFINISHED("未完成"),
    /**
     * 已完成
     */
    FINISHED("已完成");

    private final String description;

    TaskStatusEnum(String description) {
        this.description = description;
    }
}