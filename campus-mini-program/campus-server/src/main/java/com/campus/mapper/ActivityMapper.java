package com.campus.mapper;

import com.campus.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@SuppressWarnings("all")
@Mapper
public interface ActivityMapper {

    List<Activity> list(String status);

    List<Activity> listWithFilters(@Param("status") String status,
                                   @Param("type") String type,
                                   @Param("keyword") String keyword,
                                   @Param("location") String location);

    List<Activity> listHotActivities(@Param("limit") Integer limit);

    Activity getById(Long activityId);

    @Update("update activity set current_participants = current_participants + 1 where activity_id = #{activityId} and current_participants < max_participants")
    int incrParticipant(Long activityId);

    List<Activity> queryAdminList(@Param("status") String status);

    void insert(Activity activity);

    void update(Activity activity);

    void deleteById(@Param("activityId") Long activityId);

    Long countTotal();

    Long countByStatus(@Param("status") String status);

    Double avgRegistrationRate();
}
