package com.campus.controller.user;

import com.campus.entity.AiMessage;
import com.campus.result.R;
import com.campus.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户端AI智能问答相关接口
 */
@RestController
@RequestMapping("/user/ai")
@Slf4j
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    /**
     * 获取会话的消息列表
     * @param sessionId 会话id
     * @return 消息列表
     */
    @GetMapping("/message/{sessionId}")
    public R<List<AiMessage>> getMessage(@PathVariable Long sessionId) {
        log.info("获取AI会话消息，sessionId:{}", sessionId);
        List<AiMessage> list = aiService.getSessionMessage(sessionId);
        return R.success(list);
    }

    /**
     * 发送AI消息
     * @param params 参数，包含sessionId和content
     * @return AI的回复
     */
    @PostMapping("/send")
    public R<String> sendMessage(@RequestBody Map<String, Object> params) {
        Long sessionId = params.get("sessionId") != null ? Long.valueOf(params.get("sessionId").toString()) : null;
        String content = params.get("content").toString();
        log.info("发送AI消息，sessionId:{}, content:{}", sessionId, content);
        String reply = aiService.sendMessage(sessionId, content);
        return R.success(reply);
    }
}