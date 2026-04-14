package com.campus.mapper;

import com.campus.entity.CourseReminder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseReminderMapper {

    CourseReminder getById(@Param("reminderId") Long reminderId);

    CourseReminder getByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    List<CourseReminder> listByUserId(@Param("userId") Long userId);

    List<CourseReminder> listNeedSend();

    void insert(CourseReminder reminder);

    void update(CourseReminder reminder);

    void updateSend(@Param("reminderId") Long reminderId);

    void deleteById(@Param("reminderId") Long reminderId);

    void deleteByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
