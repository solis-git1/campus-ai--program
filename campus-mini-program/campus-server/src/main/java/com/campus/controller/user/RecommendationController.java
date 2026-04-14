package com.campus.controller.user;

import com.campus.entity.Recommendation;
import com.campus.result.R;
import com.campus.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端AI智能推荐相关接口
 */
@RestController
@RequestMapping("/user/recommendation")
@Slf4j
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * 获取我的推荐列表
     * @return 推荐列表
     */
    @GetMapping("/my")
    public R<List<Recommendation>> getMy() {
        log.info("获取我的推荐列表");
        List<Recommendation> list = recommendationService.getMyRecommendation();
        return R.success(list);
    }

    /**
     * 标记推荐为已点击
     * @param recId 推荐id
     * @return 成功结果
     */
    @PostMapping("/click/{recId}")
    public R markClicked(@PathVariable Long recId) {
        log.info("标记推荐已点击，recId:{}", recId);
        recommendationService.markClicked(recId);
        return R.success();
    }
}