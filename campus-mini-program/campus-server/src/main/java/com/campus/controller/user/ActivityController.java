package com.campus.controller.user;

import com.campus.entity.Activity;
import com.campus.result.R;
import com.campus.service.ActivityService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端校园活动相关接口
 */
@RestController
@RequestMapping("/user/activity")
@Slf4j
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    /**
     * 分页查询活动列表（支持多维度筛选）
     * @param status 活动状态，可选（upcoming/ongoing/completed）
     * @param type 活动类型，可选（讲座/比赛/社团活动等）
     * @param keyword 关键词搜索（标题/描述），可选
     * @param location 活动地点，可选
     * @param page 页码，默认1
     * @param pageSize 分页大小，默认10
     * @return 分页结果
     */
    @GetMapping("/list")
    public R<PageInfo<Activity>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        log.info("查询活动列表，status:{}, type:{}, keyword:{}, location:{}", status, type, keyword, location);
        PageInfo<Activity> pageInfo = activityService.listActivity(status, type, keyword, location, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 获取活动详情
     * @param activityId 活动id
     * @return 活动详情
     */
    @GetMapping("/{activityId}")
    public R<Activity> detail(@PathVariable Long activityId) {
        log.info("查看活动详情，activityId:{}", activityId);
        Activity activity = activityService.getActivityDetail(activityId);
        return R.success(activity);
    }

    /**
     * 获取热门活动推荐
     * @param limit 返回数量，默认10
     * @return 热门活动列表
     */
    @GetMapping("/hot")
    public R<List<Activity>> hot(@RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取热门活动，limit:{}", limit);
        List<Activity> activities = activityService.getHotActivities(limit);
        return R.success(activities);
    }

    /**
     * 活动报名
     * @param activityId 活动id
     * @return 成功结果
     */
    @PostMapping("/register/{activityId}")
    public R register(@PathVariable Long activityId) {
        log.info("活动报名，activityId:{}", activityId);
        activityService.registerActivity(activityId);
        return R.success();
    }

    /**
     * 收藏活动
     * @param activityId 活动id
     * @return 成功结果
     */
    @PostMapping("/favorite/{activityId}")
    public R favorite(@PathVariable Long activityId) {
        log.info("收藏活动，activityId:{}", activityId);
        activityService.favoriteActivity(activityId);
        return R.success();
    }

    /**
     * 取消收藏活动
     * @param activityId 活动id
     * @return 成功结果
     */
    @DeleteMapping("/favorite/{activityId}")
    public R unfavorite(@PathVariable Long activityId) {
        log.info("取消收藏活动，activityId:{}", activityId);
        activityService.unfavoriteActivity(activityId);
        return R.success();
    }

    /**
     * 获取用户收藏的活动ID列表
     * @return 收藏的活动ID列表
     */
    @GetMapping("/favorites")
    public R<List<Long>> favorites() {
        log.info("获取收藏的活动列表");
        List<Long> ids = activityService.getFavoriteActivityIds();
        return R.success(ids);
    }

    /**
     * 检查是否已收藏活动
     * @param activityId 活动id
     * @return 是否已收藏
     */
    @GetMapping("/check-favorite/{activityId}")
    public R<Boolean> checkFavorite(@PathVariable Long activityId) {
        log.info("检查是否收藏活动，activityId:{}", activityId);
        boolean favorited = activityService.isFavorited(activityId);
        return R.success(favorited);
    }
}