package com.campus.mapper;

import com.campus.entity.ActivityFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ActivityFavoriteMapper {

    void insert(ActivityFavorite favorite);

    void delete(@Param("userId") Long userId, @Param("activityId") Long activityId);

    ActivityFavorite getByUserAndActivity(@Param("userId") Long userId, @Param("activityId") Long activityId);

    List<ActivityFavorite> listByUserId(@Param("userId") Long userId);

    boolean existsByUserAndActivity(@Param("userId") Long userId, @Param("activityId") Long activityId);
}
