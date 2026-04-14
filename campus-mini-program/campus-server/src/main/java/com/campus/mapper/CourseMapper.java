package com.campus.mapper;

import com.campus.entity.Course;
import com.campus.vo.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {

    List<Course> listAll();

    Course getById(@Param("courseId") Long courseId);

    List<Course> queryAdminList(@Param("keyword") String keyword, @Param("type") String type, @Param("weekday") Integer weekday);

    void insert(Course course);

    void update(Course course);

    void deleteById(@Param("courseId") Long courseId);

    Long countTotal();

    Long countByType(@Param("type") String type);

    List<Course> listByUserId(Long userId);

    List<CourseVO> listUserCourse(@Param("userId") Long userId);

    CourseVO getUserCourseDetail(@Param("userId") Long userId, @Param("courseId") Long courseId);
}
