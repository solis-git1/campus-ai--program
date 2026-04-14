package com.campus.mapper;

import com.campus.entity.ClassroomOccupy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassroomOccupyMapper {

    ClassroomOccupy getById(@Param("occupyId") Long occupyId);

    void insert(ClassroomOccupy occupy);

    void update(ClassroomOccupy occupy);

    void deleteById(@Param("occupyId") Long occupyId);

    List<ClassroomOccupy> queryByClassroomId(@Param("classroomId") Long classroomId);

    void deleteByClassroomId(@Param("classroomId") Long classroomId);
}
