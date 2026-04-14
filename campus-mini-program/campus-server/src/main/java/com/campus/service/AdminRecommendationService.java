package com.campus.service;

import com.campus.dto.admin.RecommendationConfigDTO;
import com.campus.entity.Recommendation;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

/**
 * 管理端推荐管理业务接口
 */
public interface AdminRecommendationService {

    /**
     * 分页查询推荐记录
     * @param recType 推荐类型
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    PageInfo<Recommendation> listRecommendations(String recType, Integer page, Integer pageSize);

    /**
     * 获取推荐配置列表
     * @return 配置列表
     */
    List<Map<String, Object>> listRecommendationConfigs();

    /**
     * 更新推荐算法配置
     * @param dto 配置参数
     */
    void updateRecommendationConfig(RecommendationConfigDTO dto);

    /**
     * 获取推荐效果统计（点击率、转化率等）
     * @return 统计数据
     */
    Map<String, Object> getRecommendationStats();

    /**
     * 手动触发推荐刷新
     */
    void refreshRecommendations();
}
