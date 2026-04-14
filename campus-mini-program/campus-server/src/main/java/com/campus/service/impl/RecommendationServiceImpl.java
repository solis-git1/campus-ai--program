package com.campus.service.impl;

import com.campus.context.BaseContext;
import com.campus.entity.Recommendation;
import com.campus.exception.BaseException;
import com.campus.mapper.RecommendationMapper;
import com.campus.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI智能推荐业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationMapper recommendationMapper;

    /**
     * 获取当前用户的推荐列表
     */
    @Override
    public List<Recommendation> getMyRecommendation() {
        Long userId = BaseContext.getCurrentId();
        return recommendationMapper.listByUserId(userId);
    }

    /**
     * 标记推荐为已点击
     */
    @Override
    public void markClicked(Long recId) {
        Long userId = BaseContext.getCurrentId();
        int rows = recommendationMapper.markClicked(recId, userId);
        if (rows == 0) {
            throw new BaseException("推荐记录不存在或无权操作");
        }
    }
}