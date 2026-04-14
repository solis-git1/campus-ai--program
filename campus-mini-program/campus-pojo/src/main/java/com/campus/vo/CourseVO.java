package com.campus.vo;

import lombok.Data;
import java.time.LocalTime;
import java.io.Serializable;

/**
 * 课程信息视图对象
 * 
 * <p>该类用于封装课程相关的详细信息，包括课程基本信息、
 * 上课时间地点以及用户个性化数据，主要用于课程表展示。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class CourseVO implements Serializable {
    /**
     * 课程唯一标识ID
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
     * 教室名称，通过关联查询获得
     */
    private String classroomName;
    
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
     * 用户自定义备注信息
     */
    private String note;
    
    /**
     * 课程状态：current(当前课程)/completed(已完成课程)
     */
    private String status;
}