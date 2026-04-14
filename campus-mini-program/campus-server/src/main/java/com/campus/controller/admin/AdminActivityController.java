package com.campus.controller.admin;

import com.campus.dto.admin.ActivityDTO;
import com.campus.dto.admin.RegistrationStatusDTO;
import com.campus.entity.Activity;
import com.campus.entity.ActivityRegistration;
import com.campus.result.R;
import com.campus.service.AdminActivityService;
import com.campus.vo.admin.ActivityStatisticsVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端活动管理接口
 */
@RestController
@RequestMapping("/admin/activity")
@Slf4j
@RequiredArgsConstructor
public class AdminActivityController {

    private final AdminActivityService adminActivityService;

    /**
     * 分页查询活动列表
     */
    @GetMapping("/list")
    public R<PageInfo<Activity>> list(@RequestParam(required = false) String status,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询活动列表");
        PageInfo<Activity> pageInfo = adminActivityService.listActivities(status, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 查询活动详情
     */
    @GetMapping("/{activityId}")
    public R<Activity> detail(@PathVariable Long activityId) {
        log.info("管理员查看活动详情，activityId:{}", activityId);
        Activity activity = adminActivityService.getActivityDetail(activityId);
        return R.success(activity);
    }

    /**
     * 创建活动
     */
    @PostMapping
    public R<Long> create(@RequestBody @Valid ActivityDTO dto) {
        log.info("管理员创建活动");
        Long activityId = adminActivityService.createActivity(dto);
        return R.success(activityId);
    }

    /**
     * 更新活动
     */
    @PutMapping
    public R update(@RequestBody @Valid ActivityDTO dto) {
        log.info("管理员更新活动");
        adminActivityService.updateActivity(dto);
        return R.success();
    }

    /**
     * 删除活动
     */
    @DeleteMapping("/{activityId}")
    public R delete(@PathVariable Long activityId) {
        log.info("管理员删除活动，activityId:{}", activityId);
        adminActivityService.deleteActivity(activityId);
        return R.success();
    }

    /**
     * 查询报名列表
     */
    @GetMapping("/{activityId}/registrations")
    public R<PageInfo<ActivityRegistration>> registrations(@PathVariable Long activityId,
                                                            @RequestParam(required = false) Integer page,
                                                            @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询报名列表");
        PageInfo<ActivityRegistration> pageInfo = adminActivityService.listRegistrations(activityId, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 更新报名状态
     */
    @PutMapping("/registration/status")
    public R updateRegistrationStatus(@RequestBody RegistrationStatusDTO dto) {
        log.info("管理员更新报名状态");
        adminActivityService.updateRegistrationStatus(dto);
        return R.success();
    }

    /**
     * 获取活动统计
     */
    @GetMapping("/statistics")
    public R<ActivityStatisticsVO> statistics() {
        log.info("获取活动统计数据");
        ActivityStatisticsVO vo = adminActivityService.getActivityStatistics();
        return R.success(vo);
    }
}
