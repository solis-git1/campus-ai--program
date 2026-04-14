package com.campus.mapper;

import com.campus.entity.ActivityRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityRegistrationMapper {

    ActivityRegistration getByUserAndActivity(Long userId, Long activityId);

    void insert(ActivityRegistration registration);

    ActivityRegistration getById(@Param("registrationId") Long registrationId);

    void update(ActivityRegistration registration);

    List<ActivityRegistration> queryByActivityId(@Param("activityId") Long activityId);

    Long countByActivityId(@Param("activityId") Long activityId);

    Long countTotal();
}
