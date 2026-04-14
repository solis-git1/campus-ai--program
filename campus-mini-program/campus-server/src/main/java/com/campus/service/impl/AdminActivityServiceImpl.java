package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.ActivityDTO;
import com.campus.dto.admin.RegistrationStatusDTO;
import com.campus.entity.Activity;
import com.campus.entity.ActivityRegistration;
import com.campus.enumeration.ActivityStatusEnum;
import com.campus.exception.BaseException;
import com.campus.mapper.ActivityMapper;
import com.campus.mapper.ActivityRegistrationMapper;
import com.campus.service.AdminActivityService;
import com.campus.vo.admin.ActivityStatisticsVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminActivityServiceImpl implements AdminActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityRegistrationMapper registrationMapper;

    @Override
    public PageInfo<Activity> listActivities(String status, Integer page, Integer pageSize) {
        log.info("管理员查询活动列表，status:{}, page:{}, pageSize:{}", status, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<Activity> list = activityMapper.queryAdminList(status);
        return new PageInfo<>(list);
    }

    @Override
    public Activity getActivityDetail(Long activityId) {
        log.info("管理员查看活动详情，activityId:{}", activityId);

        if (activityId == null) {
            throw new BaseException(400, "活动id不能为空");
        }

        Activity activity = activityMapper.getById(activityId);
        if (activity == null) {
            throw new BaseException(404, "活动不存在，请检查活动id是否正确");
        }
        return activity;
    }

    @Override
    public Long createActivity(ActivityDTO dto) {
        log.info("管理员创建活动：{}", dto.getTitle());

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BaseException(400, "活动标题不能为空");
        }
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new BaseException(400, "请输入活动的开始时间和结束时间");
        }
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BaseException(400, "结束时间不能早于开始时间");
        }
        if (dto.getMaxParticipants() != null && dto.getMaxParticipants() <= 0) {
            throw new BaseException(400, "活动名额必须大于0");
        }

        Activity activity = new Activity();
        activity.setTitle(dto.getTitle());
        activity.setType(dto.getType());
        activity.setLocation(dto.getLocation());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setOrganizer(dto.getOrganizer());
        activity.setDescription(dto.getDescription());
        activity.setContact(dto.getContact());
        activity.setMaxParticipants(dto.getMaxParticipants());
        activity.setCurrentParticipants(0);
        activity.setStatus(dto.getStatus() != null ? dto.getStatus() : ActivityStatusEnum.UPCOMING.getCode());

        activityMapper.insert(activity);
        log.info("活动创建成功，activityId:{}", activity.getActivityId());
        return activity.getActivityId();
    }

    @Override
    public void updateActivity(ActivityDTO dto) {
        log.info("管理员更新活动，activityId:{}", dto.getActivityId());

        if (dto.getActivityId() == null) {
            throw new BaseException(400, "活动id不能为空");
        }

        Activity exist = activityMapper.getById(dto.getActivityId());
        if (exist == null) {
            throw new BaseException(404, "要更新的活动不存在，请检查活动id是否正确");
        }

        if (dto.getEndTime() != null && dto.getStartTime() != null
                && dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BaseException(400, "结束时间不能早于开始时间");
        }
        if (dto.getMaxParticipants() != null && dto.getMaxParticipants() <= 0) {
            throw new BaseException(400, "活动名额必须大于0");
        }

        Activity activity = new Activity();
        activity.setActivityId(dto.getActivityId());
        activity.setTitle(dto.getTitle());
        activity.setType(dto.getType());
        activity.setLocation(dto.getLocation());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setOrganizer(dto.getOrganizer());
        activity.setDescription(dto.getDescription());
        activity.setContact(dto.getContact());
        activity.setMaxParticipants(dto.getMaxParticipants());
        if (dto.getStatus() != null) {
            activity.setStatus(dto.getStatus());
        }

        activityMapper.update(activity);
        log.info("活动更新成功");
    }

    @Override
    public void deleteActivity(Long activityId) {
        log.info("管理员删除活动，activityId:{}", activityId);

        if (activityId == null) {
            throw new BaseException(400, "活动id不能为空");
        }

        Activity exist = activityMapper.getById(activityId);
        if (exist == null) {
            throw new BaseException(404, "要删除的活动不存在，请检查活动id是否正确");
        }

        Long regCount = registrationMapper.countByActivityId(activityId);
        if (regCount > 0) {
            throw new BaseException(409, "该活动已有" + regCount + "人报名，无法删除。如需删除请先取消所有报名记录");
        }

        activityMapper.deleteById(activityId);
        log.info("活动删除成功");
    }

    @Override
    public PageInfo<ActivityRegistration> listRegistrations(Long activityId, Integer page, Integer pageSize) {
        log.info("管理员查询报名列表，activityId:{}, page:{}, pageSize:{}", activityId, page, pageSize);

        if (activityId == null) {
            throw new BaseException(400, "活动id不能为空");
        }

        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<ActivityRegistration> list = registrationMapper.queryByActivityId(activityId);
        return new PageInfo<>(list);
    }

    @Override
    public void updateRegistrationStatus(RegistrationStatusDTO dto) {
        log.info("管理员更新报名状态，registrationId:{}, status:{}", dto.getRegistrationId(), dto.getStatus());

        if (dto.getRegistrationId() == null) {
            throw new BaseException(400, "报名记录id不能为空");
        }

        ActivityRegistration reg = registrationMapper.getById(dto.getRegistrationId());
        if (reg == null) {
            throw new BaseException(404, "该报名记录不存在，请检查记录id是否正确");
        }
        reg.setStatus(dto.getStatus());
        registrationMapper.update(reg);
        log.info("报名状态更新成功");
    }

    @Override
    public ActivityStatisticsVO getActivityStatistics() {
        log.info("获取活动统计数据");
        ActivityStatisticsVO vo = new ActivityStatisticsVO();
        vo.setTotalActivities(activityMapper.countTotal());
        vo.setOngoingActivities(activityMapper.countByStatus(ActivityStatusEnum.ONGOING.getCode()));
        vo.setCompletedActivities(activityMapper.countByStatus(ActivityStatusEnum.COMPLETED.getCode()));
        vo.setTotalRegistrations(registrationMapper.countTotal());
        vo.setAvgRegistrationRate(activityMapper.avgRegistrationRate());
        return vo;
    }
}