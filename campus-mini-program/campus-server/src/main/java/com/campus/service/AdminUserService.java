package com.campus.service;

import com.campus.dto.admin.UserQueryDTO;
import com.campus.dto.admin.UserStatusDTO;
import com.campus.entity.User;
import com.campus.vo.admin.UserStatisticsVO;
import com.github.pagehelper.PageInfo;

/**
 * 管理端用户管理业务接口
 */
public interface AdminUserService {

    /**
     * 分页查询用户列表
     * @param queryDTO 查询条件
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    PageInfo<User> listUsers(UserQueryDTO queryDTO, Integer page, Integer pageSize);

    /**
     * 根据用户ID查询用户详情
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserDetail(Long userId);

    /**
     * 更新用户状态（启用/禁用）
     * @param dto 状态更新参数
     */
    void updateUserStatus(UserStatusDTO dto);

    /**
     * 获取用户统计数据
     * @return 统计数据
     */
    UserStatisticsVO getUserStatistics();

    /**
     * 重置用户密码
     * @param userId 用户ID
     */
    void resetUserPassword(Long userId);
}
