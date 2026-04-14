package com.campus.service;

import com.campus.vo.CourseVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程业务接口
 */
public interface CourseService {

    /**
     * 获取当前用户的课表（含状态标记）
     * @return 课表列表
     */
    List<CourseVO> getMyCourse();

    /**
     * 获取课程详情
     * @param courseId 课程id
     * @return 课程详情
     */
    CourseVO getCourseDetail(Long courseId);

    /**
     * 添加课程到我的课表
     * @param courseId 课程id
     * @param note 备注
     */
    void addCourseToMy(Long courseId, String note);

    /**
     * 从我的课表删除课程
     * @param courseId 课程id
     */
    void removeCourseFromMy(Long courseId);

    /**
     * 设置课程提醒
     * @param courseId 课程id
     * @param remindTime 提醒时间
     * @param repeatType 重复类型
     */
    void setCourseReminder(Long courseId, LocalDateTime remindTime, String repeatType);
}