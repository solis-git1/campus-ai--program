package com.campus.mapper;

import com.campus.entity.Classroom;
import com.campus.vo.ClassroomVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface ClassroomMapper {

    List<Classroom> listAll();

    List<Classroom> listEmptyClassroom(LocalDate date, LocalTime startTime, LocalTime endTime, Integer dayOfWeek);

    List<ClassroomVO> listEmptyWithFilters(LocalDate date, LocalTime startTime, LocalTime endTime,
                                           @Param("dayOfWeek") Integer dayOfWeek,
                                           @Param("buildingId") Long buildingId,
                                           @Param("floor") Integer floor,
                                           @Param("minCapacity") Integer minCapacity,
                                           @Param("hasMedia") Integer hasMedia);

    Classroom getById(@Param("classroomId") Long classroomId);

    List<Classroom> queryAdminList(@Param("buildingId") Long buildingId, @Param("status") String status);

    void insert(Classroom classroom);

    void update(Classroom classroom);

    void deleteById(@Param("classroomId") Long classroomId);

    Classroom getByRoomNumberAndBuilding(@Param("roomNumber") String roomNumber, @Param("buildingId") Long buildingId);

    Long countCoursesByClassroom(@Param("classroomId") Long classroomId);
}
