package com.campus.controller.admin;

import com.campus.dto.admin.UserQueryDTO;
import com.campus.dto.admin.UserStatusDTO;
import com.campus.entity.User;
import com.campus.result.R;
import com.campus.service.AdminUserService;
import com.campus.vo.admin.UserStatisticsVO;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端用户管理接口
 */
@RestController
@RequestMapping("/admin/user")
@Slf4j
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public R<PageInfo<User>> list(UserQueryDTO queryDTO,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询用户列表");
        PageInfo<User> pageInfo = adminUserService.listUsers(queryDTO, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 查询用户详情
     */
    @GetMapping("/{userId}")
    public R<User> detail(@PathVariable Long userId) {
        log.info("管理员查看用户详情，userId:{}", userId);
        User user = adminUserService.getUserDetail(userId);
        return R.success(user);
    }

    /**
     * 更新用户状态（启用/禁用）
     */
    @PutMapping("/status")
    public R updateStatus(@RequestBody UserStatusDTO dto) {
        log.info("管理员更新用户状态");
        adminUserService.updateUserStatus(dto);
        return R.success();
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{userId}/reset-password")
    public R resetPassword(@PathVariable Long userId) {
        log.info("管理员重置用户密码，userId:{}", userId);
        adminUserService.resetUserPassword(userId);
        return R.success();
    }

    /**
     * 获取用户统计数据
     */
    @GetMapping("/statistics")
    public R<UserStatisticsVO> statistics() {
        log.info("获取用户统计数据");
        UserStatisticsVO vo = adminUserService.getUserStatistics();
        return R.success(vo);
    }
}
