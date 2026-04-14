package com.campus.mapper;

import com.campus.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileMapper {

    UserProfile getByUserId(Long userId);

    void insert(UserProfile profile);

    void update(UserProfile profile);
}
