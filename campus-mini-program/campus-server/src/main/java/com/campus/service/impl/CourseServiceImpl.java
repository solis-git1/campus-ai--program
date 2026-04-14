package com.campus.service.impl;

import com.campus.context.BaseContext;
import com.campus.entity.Course;
import com.campus.entity.CourseReminder;
import com.campus.entity.UserCourse;
import com.campus.exception.BaseException;
import com.campus.mapper.CourseMapper;
import com.campus.mapper.CourseReminderMapper;
import com.campus.mapper.UserCourseMapper;
import com.campus.service.CourseService;
import com.campus.vo.CourseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 课程业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final UserCourseMapper userCourseMapper;
    private final CourseReminderMapper courseReminderMapper;

    /**
     * 获取当前用户的课表（含状态标记）
     * 关联查询课程信息和用户的备注
     */
    @Override
    public List<CourseVO> getMyCourse() {
        Long userId = BaseContext.getCurrentId();
        List<CourseVO> courses = courseMapper.listUserCourse(userId);
        courses.forEach(this::markCourseStatus);
        return courses;
    }

    /**
     * 获取课程详情
     */
    @Override
    public CourseVO getCourseDetail(Long courseId) {
        if (courseId == null) {
            throw new BaseException(400, "课程id不能为空");
        }
        Long userId = BaseContext.getCurrentId();
        CourseVO vo = courseMapper.getUserCourseDetail(userId, courseId);
        if (vo == null) {
            throw new BaseException(404, "该课程不在你的课表中");
        }
        markCourseStatus(vo);
        return vo;
    }

    /**
     * 标记课程状态：根据当前时间和课程的星期、时间段判断是"当前"还是"已完成"
     */
    private void markCourseStatus(CourseVO course) {
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();
        LocalTime now = LocalTime.now();

        int currentWeekday = today.getValue();
        Integer courseWeekday = course.getWeekday();

        if (courseWeekday != null && courseWeekday < currentWeekday) {
            course.setStatus("completed");
        } else if (courseWeekday != null && courseWeekday.equals(currentWeekday)) {
            if (course.getEndTime() != null && now.isAfter(course.getEndTime())) {
                course.setStatus("completed");
            } else {
                course.setStatus("current");
            }
        } else {
            course.setStatus("current");
        }
    }

    /**
     * 添加课程到我的课表
     */
    @Override
    public void addCourseToMy(Long courseId, String note) {
        Long userId = BaseContext.getCurrentId();

        Course course = courseMapper.getById(courseId);
        if (course == null) {
            throw new BaseException(404, "课程不存在");
        }

        UserCourse existing = userCourseMapper.getByUserAndCourse(userId, courseId);
        if (existing != null) {
            throw new BaseException(409, "该课程已在你的课表中");
        }

        UserCourse userCourse = new UserCourse();
        userCourse.setUserId(userId);
        userCourse.setCourseId(courseId);
        userCourse.setNote(note);
        userCourse.setIsCustom(0);
        userCourseMapper.insert(userCourse);
    }

    /**
     * 从我的课表删除课程
     */
    @Override
    public void removeCourseFromMy(Long courseId) {
        Long userId = BaseContext.getCurrentId();
        userCourseMapper.delete(userId, courseId);
    }

    /**
     * 设置课程提醒
     */
    @Override
    public void setCourseReminder(Long courseId, LocalDateTime remindTime, String repeatType) {
        Long userId = BaseContext.getCurrentId();
        // 检查是否已有提醒，有则覆盖
        CourseReminder oldReminder = courseReminderMapper.getByUserAndCourse(userId, courseId);
        if (oldReminder != null) {
            courseReminderMapper.deleteByUserAndCourse(userId, courseId);
        }
        CourseReminder reminder = new CourseReminder();
        reminder.setUserId(userId);
        reminder.setCourseId(courseId);
        reminder.setRemindTime(remindTime);
        reminder.setRepeatType(repeatType);
        courseReminderMapper.insert(reminder);
    }
}