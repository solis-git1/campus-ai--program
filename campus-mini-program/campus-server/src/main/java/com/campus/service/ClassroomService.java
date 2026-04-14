package com.campus.service;

import com.campus.entity.Classroom;
import com.campus.vo.ClassroomVO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ClassroomService {

    List<Classroom> listAll();

    List<Classroom> listEmptyClassroom(LocalDate date, LocalTime startTime, LocalTime endTime);

    List<Classroom> listAllWithBehavior();

    List<Classroom> listEmptyClassroomWithBehavior(LocalDate date, LocalTime startTime, LocalTime endTime);

    /**
     * 多条件筛选空教室
     * @param date 日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param buildingId 教学楼ID，可选
     * @param floor 楼层，可选
     * @param minCapacity 最小座位数，可选
     * @param hasMedia 是否多媒体，可选（1:是 0:否）
     * @return 空教室列表（含推荐指数）
     */
    List<ClassroomVO> listEmptyWithFilters(LocalDate date, LocalTime startTime, LocalTime endTime,
                                           Long buildingId, Integer floor, Integer minCapacity, Integer hasMedia);

    void favoriteClassroom(Long classroomId);

    void unfavoriteClassroom(Long classroomId);

    List<Long> getFavoriteClassroomIds();

    boolean isFavorited(Long classroomId);
}
