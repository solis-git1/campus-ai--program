package com.campus.mapper;

import com.campus.entity.AiMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@SuppressWarnings("all")
@Mapper
public interface AiMessageMapper {

    List<AiMessage> listBySessionId(Long sessionId, Long userId);

    void insert(AiMessage message);

    List<AiMessage> queryBySessionId(@Param("sessionId") Long sessionId);

    Long countTotal();

    Double avgPerSession();

    List<Map<String, Object>> topQuestions(@Param("limit") int limit);

    List<Map<String, Object>> questionCountByHour();
}
