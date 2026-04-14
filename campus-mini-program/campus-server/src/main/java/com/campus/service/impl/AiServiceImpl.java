package com.campus.service.impl;

import com.campus.context.BaseContext;
import com.campus.entity.AiChatSession;
import com.campus.entity.AiMessage;
import com.campus.exception.BaseException;
import com.campus.mapper.AiChatSessionMapper;
import com.campus.mapper.AiMessageMapper;
import com.campus.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final AiChatSessionMapper sessionMapper;
    private final AiMessageMapper messageMapper;

    @Override
    public List<AiMessage> getSessionMessage(Long sessionId) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BaseException(401, "请先登录");
        }
        if (sessionId == null) {
            throw new BaseException(400, "会话ID不能为空");
        }
        return messageMapper.listBySessionId(sessionId, userId);
    }

    @Override
    public String sendMessage(Long sessionId, String content) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BaseException(401, "请先登录");
        }
        if (content == null || content.isBlank()) {
            throw new BaseException(400, "消息内容不能为空");
        }

        if (sessionId == null) {
            AiChatSession session = new AiChatSession();
            session.setUserId(userId);
            String title = content.length() > 20 ? content.substring(0, 20) : content;
            session.setTitle(title);
            session.setCreateTime(LocalDateTime.now());
            sessionMapper.insert(session);
            sessionId = session.getSessionId();
        }

        AiMessage userMsg = new AiMessage();
        userMsg.setSessionId(sessionId);
        userMsg.setUserId(userId);
        userMsg.setRole("user");
        userMsg.setContent(content);
        userMsg.setCreateTime(LocalDateTime.now());
        messageMapper.insert(userMsg);

        log.info("AI消息请求: userId={}, sessionId={}", userId, sessionId);

        String aiReply = "你好，我已经收到你的问题，我会尽快为你解答。";

        AiMessage aiMsg = new AiMessage();
        aiMsg.setSessionId(sessionId);
        aiMsg.setUserId(userId);
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiReply);
        aiMsg.setCreateTime(LocalDateTime.now());
        messageMapper.insert(aiMsg);

        return aiReply;
    }
}
