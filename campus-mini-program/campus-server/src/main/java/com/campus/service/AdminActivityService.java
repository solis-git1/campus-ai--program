package com.campus.service;

import com.campus.dto.admin.ActivityDTO;
import com.campus.dto.admin.RegistrationStatusDTO;
import com.campus.entity.Activity;
import com.campus.entity.ActivityRegistration;
import com.campus.vo.admin.ActivityStatisticsVO;
import com.github.pagehelper.PageInfo;

/**
 * 管理端活动管理业务接口
 */
public interface AdminActivityService {

    /**
     * 分页查询活动列表
     * @param status 状态筛选
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    PageInfo<Activity> listActivities(String status, Integer page, Integer pageSize);

    /**
     * 根据ID查询活动详情
     * @param activityId 活动ID
     * @return 活动信息
     */
    Activity getActivityDetail(Long activityId);

    /**
     * 创建活动
     * @param dto 活动参数
     * @return 活动ID
     */
    Long createActivity(ActivityDTO dto);

    /**
     * 更新活动
     * @param dto 活动参数
     */
    void updateActivity(ActivityDTO dto);

    /**
     * 删除活动
     * @param activityId 活动ID
     */
    void deleteActivity(Long activityId);

    /**
     * 查询活动的报名列表
     * @param activityId 活动ID
     * @param page 页码
     * @param pageSize 分页大小
     * @return 报名列表
     */
    PageInfo<ActivityRegistration> listRegistrations(Long activityId, Integer page, Integer pageSize);

    /**
     * 更新报名状态
     * @param dto 报名状态参数
     */
    void updateRegistrationStatus(RegistrationStatusDTO dto);

    /**
     * 获取活动统计数据
     * @return 统计数据
     */
    ActivityStatisticsVO getActivityStatistics();
}
