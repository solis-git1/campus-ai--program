package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.AdminLoginDTO;
import com.campus.entity.User;
import com.campus.exception.BaseException;
import com.campus.mapper.UserMapper;
import com.campus.service.AdminService;
import com.campus.utils.JwtUtil;
import com.campus.utils.PasswordUtil;
import com.campus.vo.admin.AdminLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;

    /**
     * 管理员登录
     */
    @Override
    public AdminLoginVO login(AdminLoginDTO dto) {
        log.info("管理员登录：{}", dto.getUsername());
        // 1. 根据用户名查询用户
        User user = userMapper.getByUsername(dto.getUsername());
        if (user == null) {
            throw new BaseException("用户不存在");
        }

        // 2. 验证密码
        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword())) {
            throw new BaseException("密码错误");
        }

        // 3. 验证是否是管理员角色
        if (!"admin".equals(user.getRole())) {
            throw new BaseException("权限不足，需要管理员账号");
        }

        // 4. 检查账号状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BaseException("账号已被禁用，请联系超级管理员");
        }

        // 5. 生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        String token = JwtUtil.generateToken(claims);

        // 6. 封装返回结果
        AdminLoginVO vo = new AdminLoginVO();
        vo.setAdminId(user.getUserId());
        vo.setToken(token);
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());

        log.info("管理员登录成功，userId:{}", user.getUserId());
        return vo;
    }
}
