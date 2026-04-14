package com.campus.service;

import com.campus.dto.admin.AiFeedbackManageDTO;
import com.campus.entity.AiChatSession;
import com.campus.entity.AiFeedback;
import com.campus.entity.AiMessage;
import com.github.pagehelper.PageInfo;
import java.util.Map;

/**
 * 管理端AI会话管理业务接口
 */
public interface AdminAiService {

    /**
     * 分页查询AI会话列表
     * @param userId 用户ID（可选）
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    PageInfo<AiChatSession> listSessions(Long userId, Integer page, Integer pageSize);

    /**
     * 查询会话的消息列表
     * @param sessionId 会话ID
     * @param page 页码
     * @param pageSize 分页大小
     * @return 消息列表
     */
    PageInfo<AiMessage> listMessages(Long sessionId, Integer page, Integer pageSize);

    /**
     * 分页查询用户反馈列表
     * @param status 处理状态（可选）
     * @param page 页码
     * @param pageSize 分页大小
     * @return 反馈列表
     */
    PageInfo<AiFeedback> listFeedbacks(String status, Integer page, Integer pageSize);

    /**
     * 处理反馈
     * @param dto 反馈处理参数
     */
    void handleFeedback(AiFeedbackManageDTO dto);

    /**
     * 获取AI使用统计
     * @return 统计数据
     */
    Map<String, Object> getAiStatistics();

    /**
     * 获取用户提问统计（热门问题等）
     * @return 统计数据
     */
    Map<String, Object> getUserQuestionStats();
}
