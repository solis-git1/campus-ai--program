package com.campus.entity;

import lombok.Data;

/**
 * 用户课程关联
 */
@Data
public class UserCourse {
    private Long id; //主键
    private Long userId; //用户ID
    private Long courseId; //课程ID
    private String note; //课程备注
    private Integer isCustom; //是否自定义
}