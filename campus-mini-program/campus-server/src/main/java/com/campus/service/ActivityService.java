package com.campus.service;

import com.campus.entity.Activity;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 校园活动业务接口
 */
public interface ActivityService {

    /**
     * 分页查询活动列表（支持多维度筛选）
     * @param status 活动状态，可选
     * @param type 活动类型，可选
     * @param keyword 关键词搜索（标题/描述），可选
     * @param location 活动地点，可选
     * @param page 页码
     * @param pageSize 分页大小
     * @return 活动列表
     */
    PageInfo<Activity> listActivity(String status, String type, String keyword, String location,
                                    Integer page, Integer pageSize);

    /**
     * 获取活动详情
     * @param activityId 活动id
     * @return 活动详情
     */
    Activity getActivityDetail(Long activityId);

    /**
     * 获取热门活动推荐
     * @param limit 返回数量限制
     * @return 热门活动列表
     */
    List<Activity> getHotActivities(Integer limit);

    /**
     * 活动报名
     * @param activityId 活动id
     */
    void registerActivity(Long activityId);

    /**
     * 收藏活动
     * @param activityId 活动id
     */
    void favoriteActivity(Long activityId);

    /**
     * 取消收藏活动
     * @param activityId 活动id
     */
    void unfavoriteActivity(Long activityId);

    /**
     * 获取用户收藏的活动列表
     * @return 收藏的活动列表
     */
    List<Long> getFavoriteActivityIds();

    /**
     * 检查是否已收藏活动
     * @param activityId 活动id
     * @return 是否已收藏
     */
    boolean isFavorited(Long activityId);
}