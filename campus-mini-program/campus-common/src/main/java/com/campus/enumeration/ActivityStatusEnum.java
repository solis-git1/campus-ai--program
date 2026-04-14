package com.campus.enumeration;

import lombok.Getter;

/**
 * 校园活动状态枚举
 */
@Getter
public enum ActivityStatusEnum {
    /**
     * 未开始
     */
    UPCOMING("未开始", "upcoming"),
    /**
     * 进行中
     */
    ONGOING("进行中", "ongoing"),
    /**
     * 已结束
     */
    COMPLETED("已结束", "completed");

    private final String description;
    private final String code;

    ActivityStatusEnum(String description, String code) {
        this.description = description;
        this.code = code;
    }
}