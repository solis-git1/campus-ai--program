package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 课程实体类
 * 
 * <p>该类对应数据库中的课程表，用于存储校园课程的基本信息，
 * 包括课程名称、任课教师、上课时间地点等，支持课程表功能。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class Course {
    /**
     * 课程唯一标识ID，主键
     */
    private Long courseId;
    
    /**
     * 课程名称
     */
    private String courseName;
    
    /**
     * 任课教师姓名
     */
    private String teacher;
    
    /**
     * 教室ID，关联教室表
     */
    private Long classroomId;
    
    /**
     * 上课星期（1-7，对应周一至周日）
     */
    private Integer weekday;
    
    /**
     * 课程开始时间
     */
    private LocalTime startTime;
    
    /**
     * 课程结束时间
     */
    private LocalTime endTime;
    
    /**
     * 课程类型（如：必修、选修等）
     */
    private String type;
    
    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;
}