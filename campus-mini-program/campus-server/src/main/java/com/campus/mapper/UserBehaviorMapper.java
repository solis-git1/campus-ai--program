package com.campus.mapper;

import com.campus.entity.UserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@SuppressWarnings("all")
@Mapper
public interface UserBehaviorMapper {

    UserBehavior getById(@Param("behaviorId") Long behaviorId);

    List<UserBehavior> listByUserId(@Param("userId") Long userId);

    void insert(UserBehavior behavior);

    void update(UserBehavior behavior);

    void deleteById(@Param("behaviorId") Long behaviorId);

    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Map<String, Object>> statsByType(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    Long countActiveUsers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Map<String, Object>> topFeatures(@Param("limit") int limit);

    Long countByTargetTypeAndPeriod(@Param("targetType") String targetType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Map<String, Object>> newUserTrend(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("period") String period);

    List<Map<String, Object>> behaviorTrend(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("period") String period);

    List<Map<String, Object>> loginTrend(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("period") String period);
}
