package com.campus.service;

import com.campus.dto.admin.CourseDTO;
import com.campus.entity.Course;
import com.github.pagehelper.PageInfo;

/**
 * 管理端课程管理业务接口
 */
public interface AdminCourseService {

    /**
     * 分页查询课程列表
     * @param type 课程类型
     * @param weekday 星期
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    PageInfo<Course> listCourses(String type, Integer weekday, Integer page, Integer pageSize);

    /**
     * 根据ID查询课程详情
     * @param courseId 课程ID
     * @return 课程信息
     */
    Course getCourseDetail(Long courseId);

    /**
     * 创建课程
     * @param dto 课程参数
     * @return 课程ID
     */
    Long createCourse(CourseDTO dto);

    /**
     * 更新课程
     * @param dto 课程参数
     */
    void updateCourse(CourseDTO dto);

    /**
     * 删除课程
     * @param courseId 课程ID
     */
    void deleteCourse(Long courseId);

    /**
     * 同步教务课表（从外部数据源导入）
     * @return 同步的课程数量
     */
    int syncAcademicSchedule();

    /**
     * 获取所有课程类型
     * @return 课程类型列表
     */
    java.util.List<String> listCourseTypes();
}
