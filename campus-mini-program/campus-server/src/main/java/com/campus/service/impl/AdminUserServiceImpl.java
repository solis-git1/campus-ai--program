package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.UserQueryDTO;
import com.campus.dto.admin.UserStatusDTO;
import com.campus.entity.User;
import com.campus.exception.BaseException;
import com.campus.mapper.UserMapper;
import com.campus.service.AdminUserService;
import com.campus.utils.PasswordUtil;
import com.campus.vo.admin.UserStatisticsVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserMapper userMapper;

    @Override
    public PageInfo<User> listUsers(UserQueryDTO queryDTO, Integer page, Integer pageSize) {
        log.info("管理员查询用户列表，query:{}, page:{}, pageSize:{}", queryDTO, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<User> list = userMapper.queryList(queryDTO);
        list.forEach(u -> u.setPassword(null));
        return new PageInfo<>(list);
    }

    @Override
    public User getUserDetail(Long userId) {
        log.info("管理员查看用户详情，userId:{}", userId);
        User user = userMapper.getById(userId);
        if (user == null) {
            throw new BaseException(404, "用户不存在");
        }
        user.setPassword(null);
        return user;
    }

    @Override
    public void updateUserStatus(UserStatusDTO dto) {
        log.info("管理员更新用户状态，userId:{}, status:{}", dto.getUserId(), dto.getStatus());
        User user = userMapper.getById(dto.getUserId());
        if (user == null) {
            throw new BaseException(404, "用户不存在");
        }

        if (dto.getUserId().equals(Constant.ADMIN_USER_ID)) {
            throw new BaseException(403, "不能修改超级管理员的状态");
        }

        User update = new User();
        update.setUserId(dto.getUserId());
        update.setStatus(dto.getStatus());
        update.setUpdateTime(LocalDateTime.now());
        userMapper.update(update);
        log.info("用户状态更新成功");
    }

    @Override
    public UserStatisticsVO getUserStatistics() {
        log.info("获取用户统计数据");
        UserStatisticsVO vo = new UserStatisticsVO();
        vo.setTotalUsers(userMapper.countTotal());
        vo.setActiveUsers(userMapper.countActive(LocalDateTime.now().minusDays(30)));
        vo.setNewUsersToday(userMapper.countByDate(LocalDateTime.now().with(LocalTime.MIN)));
        vo.setNewUsersThisWeek(userMapper.countByDateRange(
                LocalDateTime.now().minusWeeks(1).with(java.time.DayOfWeek.MONDAY).with(LocalTime.MIN),
                LocalDateTime.now().with(LocalTime.MAX)
        ));
        vo.setStudentCount(userMapper.countByRole("student"));
        vo.setAdminCount(userMapper.countByRole("admin"));
        return vo;
    }

    @Override
    public void resetUserPassword(Long userId) {
        log.info("管理员重置用户密码，userId:{}", userId);
        User user = userMapper.getById(userId);
        if (user == null) {
            throw new BaseException(404, "用户不存在");
        }

        if (userId.equals(Constant.ADMIN_USER_ID)) {
            throw new BaseException(403, "不能重置超级管理员的密码");
        }

        User update = new User();
        update.setUserId(userId);
        update.setPassword(PasswordUtil.encode(Constant.DEFAULT_PASSWORD));
        update.setUpdateTime(LocalDateTime.now());
        userMapper.update(update);
        log.info("用户密码重置成功");
    }
}
