package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.context.BaseContext;
import com.campus.entity.Activity;
import com.campus.entity.ActivityFavorite;
import com.campus.entity.ActivityRegistration;
import com.campus.enumeration.ActivityStatusEnum;
import com.campus.exception.BaseException;
import com.campus.mapper.ActivityFavoriteMapper;
import com.campus.mapper.ActivityMapper;
import com.campus.mapper.ActivityRegistrationMapper;
import com.campus.service.ActivityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;
    private final ActivityRegistrationMapper registrationMapper;
    private final ActivityFavoriteMapper favoriteMapper;

    @Override
    public PageInfo<Activity> listActivity(String status, String type, String keyword, String location,
                                           Integer page, Integer pageSize) {
        if (pageSize == null) pageSize = Constant.DEFAULT_PAGE_SIZE;
        if (page == null) page = 1;

        PageHelper.startPage(page, pageSize);
        List<Activity> list = activityMapper.listWithFilters(status, type, keyword, location);
        return new PageInfo<>(list);
    }

    @Override
    public Activity getActivityDetail(Long activityId) {
        if (activityId == null) {
            throw new BaseException(400, "活动id不能为空");
        }
        Activity activity = activityMapper.getById(activityId);
        if (activity == null) {
            throw new BaseException(404, "活动不存在");
        }
        return activity;
    }

    @Override
    public List<Activity> getHotActivities(Integer limit) {
        if (limit == null || limit <= 0) limit = 10;
        return activityMapper.listHotActivities(limit);
    }

    @Override
    public void registerActivity(Long activityId) {
        Long userId = BaseContext.getCurrentId();

        if (activityId == null) {
            throw new BaseException(400, "活动id不能为空");
        }

        Activity activity = activityMapper.getById(activityId);
        if (activity == null) {
            throw new BaseException(404, "该活动不存在，请检查活动id是否正确");
        }

        String status = activity.getStatus();
        if (ActivityStatusEnum.COMPLETED.getCode().equals(status)) {
            throw new BaseException(409, "活动已结束，无法报名");
        }
        if (!ActivityStatusEnum.UPCOMING.getCode().equals(status)
                && !ActivityStatusEnum.ONGOING.getCode().equals(status)) {
            throw new BaseException(409, "该活动当前状态不允许报名");
        }

        ActivityRegistration reg = registrationMapper.getByUserAndActivity(userId, activityId);
        if (reg != null) {
            throw new BaseException(409, "你已经报名过该活动了");
        }

        if (activity.getMaxParticipants() != null && activity.getMaxParticipants() > 0
                && activity.getCurrentParticipants() != null
                && activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            throw new BaseException(409, "该活动名额已满，无法报名");
        }

        ActivityRegistration registration = new ActivityRegistration();
        registration.setUserId(userId);
        registration.setActivityId(activityId);
        registration.setRegisterTime(LocalDateTime.now());
        registration.setStatus("registered");
        registrationMapper.insert(registration);

        int rows = activityMapper.incrParticipant(activityId);
        if (rows == 0) {
            throw new BaseException(409, "该活动名额已满，无法报名");
        }
    }

    @Override
    public void favoriteActivity(Long activityId) {
        Long userId = BaseContext.getCurrentId();

        if (activityId == null) {
            throw new BaseException(400, "活动id不能为空");
        }

        Activity activity = activityMapper.getById(activityId);
        if (activity == null) {
            throw new BaseException(404, "活动不存在");
        }

        boolean exists = favoriteMapper.existsByUserAndActivity(userId, activityId);
        if (exists) {
            throw new BaseException(409, "你已经收藏过该活动了");
        }

        ActivityFavorite favorite = new ActivityFavorite();
        favorite.setUserId(userId);
        favorite.setActivityId(activityId);
        favorite.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite);

        log.info("用户{}收藏活动{}", userId, activityId);
    }

    @Override
    public void unfavoriteActivity(Long activityId) {
        Long userId = BaseContext.getCurrentId();

        if (activityId == null) {
            throw new BaseException(400, "活动id不能为空");
        }

        favoriteMapper.delete(userId, activityId);
        log.info("用户{}取消收藏活动{}", userId, activityId);
    }

    @Override
    public List<Long> getFavoriteActivityIds() {
        Long userId = BaseContext.getCurrentId();
        List<ActivityFavorite> favorites = favoriteMapper.listByUserId(userId);
        return favorites.stream()
                .map(ActivityFavorite::getActivityId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorited(Long activityId) {
        Long userId = BaseContext.getCurrentId();
        return favoriteMapper.existsByUserAndActivity(userId, activityId);
    }
}