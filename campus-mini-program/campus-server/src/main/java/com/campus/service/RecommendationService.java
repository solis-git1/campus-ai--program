package com.campus.service;

import com.campus.entity.Recommendation;

import java.util.List;

/**
 * AI智能推荐业务接口
 */
public interface RecommendationService {

    /**
     * 获取当前用户的推荐列表
     * @return 推荐列表
     */
    List<Recommendation> getMyRecommendation();

    /**
     * 标记推荐为已点击
     * @param recId 推荐id
     */
    void markClicked(Long recId);
}