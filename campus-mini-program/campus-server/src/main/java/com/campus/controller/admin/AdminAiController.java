package com.campus.controller.admin;

import com.campus.dto.admin.AiFeedbackManageDTO;
import com.campus.entity.AiChatSession;
import com.campus.entity.AiFeedback;
import com.campus.entity.AiMessage;
import com.campus.result.R;
import com.campus.service.AdminAiService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端AI会话管理接口
 */
@RestController
@RequestMapping("/admin/ai")
@Slf4j
@RequiredArgsConstructor
public class AdminAiController {

    private final AdminAiService adminAiService;

    /**
     * 查询AI会话列表
     */
    @GetMapping("/sessions/list")
    public R<PageInfo<AiChatSession>> listSessions(@RequestParam(required = false) Long userId,
                                                    @RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询AI会话列表");
        PageInfo<AiChatSession> pageInfo = adminAiService.listSessions(userId, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 查询会话消息列表
     */
    @GetMapping("/sessions/{sessionId}/messages")
    public R<PageInfo<AiMessage>> listMessages(@PathVariable Long sessionId,
                                                @RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询AI消息列表");
        PageInfo<AiMessage> pageInfo = adminAiService.listMessages(sessionId, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 查询反馈列表
     */
    @GetMapping("/feedbacks/list")
    public R<PageInfo<AiFeedback>> listFeedbacks(@RequestParam(required = false) String status,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询AI反馈列表");
        PageInfo<AiFeedback> pageInfo = adminAiService.listFeedbacks(status, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 处理反馈
     */
    @PutMapping("/feedbacks/handle")
    public R handleFeedback(@RequestBody AiFeedbackManageDTO dto) {
        log.info("管理员处理反馈");
        adminAiService.handleFeedback(dto);
        return R.success();
    }

    /**
     * 获取AI使用统计
     */
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        log.info("获取AI使用统计");
        Map<String, Object> stats = adminAiService.getAiStatistics();
        return R.success(stats);
    }

    /**
     * 获取用户提问统计
     */
    @GetMapping("/question-stats")
    public R<Map<String, Object>> questionStats() {
        log.info("获取用户提问统计");
        Map<String, Object> stats = adminAiService.getUserQuestionStats();
        return R.success(stats);
    }
}
