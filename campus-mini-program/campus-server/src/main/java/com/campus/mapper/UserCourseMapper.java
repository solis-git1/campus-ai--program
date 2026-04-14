package com.campus.mapper;

import com.campus.entity.UserCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserCourseMapper {

    UserCourse getById(@Param("id") Long id);

    UserCourse getByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    List<UserCourse> listByUserId(Long userId);

    void insert(UserCourse userCourse);

    void update(UserCourse userCourse);

    void delete(@Param("userId") Long userId, @Param("courseId") Long courseId);

    void deleteById(@Param("id") Long id);
}
