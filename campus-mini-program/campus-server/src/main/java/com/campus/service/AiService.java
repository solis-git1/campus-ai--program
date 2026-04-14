package com.campus.service;

import com.campus.entity.AiMessage;

import java.util.List;

/**
 * AI智能问答业务接口
 */
public interface AiService {

    /**
     * 获取会话的消息列表
     * @param sessionId 会话id
     * @return 消息列表
     */
    List<AiMessage> getSessionMessage(Long sessionId);

    /**
     * 发送AI消息
     * @param sessionId 会话id，为空则新建会话
     * @param content 消息内容
     * @return AI的回复
     */
    String sendMessage(Long sessionId, String content);
}