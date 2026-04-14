package com.campus.mapper;

import com.campus.dto.admin.UserQueryDTO;
import com.campus.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问接口
 * 
 * <p>该接口定义了用户相关的数据库操作，包括用户信息的增删改查、
 * 统计查询等功能，为Service层提供数据访问支持。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     * 
     * @param username 用户名
     * @return 用户信息，如果不存在返回null
     */
    User getByUsername(String username);

    /**
     * 根据用户ID查询用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息，如果不存在返回null
     */
    User getById(Long userId);

    /**
     * 插入用户信息
     * 
     * @param user 用户信息对象
     */
    void insert(User user);

    /**
     * 更新用户信息
     * 
     * @param user 用户信息对象
     */
    void update(User user);

    /**
     * 根据查询条件分页查询用户列表
     * 
     * @param queryDTO 用户查询条件
     * @return 用户列表
     */
    List<User> queryList(UserQueryDTO queryDTO);

    /**
     * 统计用户总数
     * 
     * @return 用户总数
     */
    Long countTotal();

    /**
     * 统计活跃用户数量（指定时间后登录过的用户）
     * 
     * @param since 起始时间
     * @return 活跃用户数量
     */
    Long countActive(LocalDateTime since);

    /**
     * 统计指定日期的用户数量
     * 
     * @param date 指定日期
     * @return 用户数量
     */
    Long countByDate(LocalDateTime date);

    /**
     * 统计指定时间范围内的用户数量
     * 
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 用户数量
     */
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * 根据角色统计用户数量
     * 
     * @param role 用户角色
     * @return 用户数量
     */
    Long countByRole(@Param("role") String role);

    /**
     * 查询所有用户列表
     * 
     * @return 所有用户列表
     */
    List<User> listAll();

    /**
     * 根据用户ID列表查询用户信息
     * 
     * @param userIds 用户ID列表
     * @return 用户列表
     */
    List<User> listByIds(@Param("userIds") List<Long> userIds);
}
