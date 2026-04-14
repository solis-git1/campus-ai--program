package com.campus.mapper;

import com.campus.entity.ClassroomFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ClassroomFavoriteMapper {

    void insert(ClassroomFavorite favorite);

    void delete(@Param("userId") Long userId, @Param("classroomId") Long classroomId);

    List<ClassroomFavorite> listByUserId(@Param("userId") Long userId);

    boolean existsByUserAndClassroom(@Param("userId") Long userId, @Param("classroomId") Long classroomId);
}
