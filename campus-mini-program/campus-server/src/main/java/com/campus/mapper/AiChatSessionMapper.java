package com.campus.mapper;

import com.campus.entity.AiChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiChatSessionMapper {

    List<AiChatSession> listByUserId(Long userId);

    void insert(AiChatSession session);

    List<AiChatSession> queryAdminList(@Param("userId") Long userId);

    AiChatSession getById(@Param("sessionId") Long sessionId);

    Long countTotal();

    Long countActiveUsersToday();
}
