package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.AiFeedbackManageDTO;
import com.campus.entity.AiChatSession;
import com.campus.entity.AiFeedback;
import com.campus.entity.AiMessage;
import com.campus.exception.BaseException;
import com.campus.mapper.AiChatSessionMapper;
import com.campus.mapper.AiFeedbackMapper;
import com.campus.mapper.AiMessageMapper;
import com.campus.service.AdminAiService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理端AI会话管理业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminAiServiceImpl implements AdminAiService {

    private final AiChatSessionMapper sessionMapper;
    private final AiMessageMapper messageMapper;
    private final AiFeedbackMapper feedbackMapper;

    /**
     * 查询会话列表
     */
    @Override
    public PageInfo<AiChatSession> listSessions(Long userId, Integer page, Integer pageSize) {
        log.info("管理员查询AI会话列表，userId:{}, page:{}, pageSize:{}", userId, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<AiChatSession> list = sessionMapper.queryAdminList(userId);
        return new PageInfo<>(list);
    }

    /**
     * 查询消息列表
     */
    @Override
    public PageInfo<AiMessage> listMessages(Long sessionId, Integer page, Integer pageSize) {
        log.info("管理员查询AI消息列表，sessionId:{}, page:{}, pageSize:{}", sessionId, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<AiMessage> list = messageMapper.queryBySessionId(sessionId);
        return new PageInfo<>(list);
    }

    /**
     * 查询反馈列表
     */
    @Override
    public PageInfo<AiFeedback> listFeedbacks(String status, Integer page, Integer pageSize) {
        log.info("管理员查询AI反馈列表，status:{}, page:{}, pageSize:{}", status, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<AiFeedback> list = feedbackMapper.queryAdminList(status);
        return new PageInfo<>(list);
    }

    /**
     * 处理反馈
     */
    @Override
    public void handleFeedback(AiFeedbackManageDTO dto) {
        log.info("管理员处理反馈，feedbackId:{}", dto.getFeedbackId());
        AiFeedback feedback = feedbackMapper.getById(dto.getFeedbackId());
        if (feedback == null) {
            throw new BaseException("反馈记录不存在");
        }

        feedback.setStatus(dto.getStatus());
        if (dto.getReply() != null) {
            feedback.setReply(dto.getReply());
        }
        feedback.setUpdateTime(LocalDateTime.now());
        feedbackMapper.update(feedback);
        log.info("反馈处理完成");
    }

    /**
     * 获取AI使用统计
     */
    @Override
    public Map<String, Object> getAiStatistics() {
        log.info("获取AI使用统计数据");
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSessions", sessionMapper.countTotal());
        stats.put("totalMessages", messageMapper.countTotal());
        stats.put("totalFeedbacks", feedbackMapper.countTotal());
        stats.put("activeUsersToday", sessionMapper.countActiveUsersToday());
        stats.put("avgMessagesPerSession", messageMapper.avgPerSession());
        return stats;
    }

    /**
     * 获取用户提问统计
     */
    @Override
    public Map<String, Object> getUserQuestionStats() {
        log.info("获取用户提问统计");
        Map<String, Object> stats = new HashMap<>();
        stats.put("topQuestions", messageMapper.topQuestions(10));
        stats.put("questionCountByHour", messageMapper.questionCountByHour());
        return stats;
    }
}
