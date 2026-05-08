package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.AdminLoginDTO;
import com.campus.entity.User;
import com.campus.exception.BaseException;
import com.campus.mapper.UserMapper;
import com.campus.service.AdminService;
// import com.campus.service.LoginAttemptService;  // 暂时注释掉
import com.campus.utils.JwtUtil;
import com.campus.utils.PasswordUtil;
import com.campus.vo.admin.AdminLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;
    // private final LoginAttemptService loginAttemptService;  // 暂时注释掉

    @Override
    public AdminLoginVO login(AdminLoginDTO dto) {
        String username = dto.getUsername();
        log.info("管理员登录：{}", username);

        // 暂时注释掉登录锁定检查
        // if (loginAttemptService.isLocked(username)) {
        //     int remaining = loginAttemptService.getRemainingAttempts(username);
        //     log.warn("管理员账号{}已被临时锁定，登录被拒绝", username);
        //     throw new BaseException(429, "登录失败次数过多，账号已临时锁定30分钟，请稍后再试");
        // }

        User user = userMapper.getByUsername(username);
        if (user == null) {
            // loginAttemptService.loginFailed(username);  // 注释掉
            throw new BaseException("用户名或密码错误");
        }

        if (!PasswordUtil.matches(dto.getPassword(), user.getPassword())) {
            // loginAttemptService.loginFailed(username);  // 注释掉
            // int remaining = loginAttemptService.getRemainingAttempts(username);
            // if (remaining <= 0) {
            //     throw new BaseException(429, "登录失败次数过多，账号已临时锁定30分钟");
            // }
            throw new BaseException("用户名或密码错误");
        }

        if (!"admin".equals(user.getRole())) {
            throw new BaseException("权限不足，需要管理员账号");
        }

        if (user.getStatus() != null && user.getStatus() == 1) {
            throw new BaseException("账号已被禁用，请联系超级管理员");
        }

        // loginAttemptService.loginSucceeded(username);  // 注释掉

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        String token = JwtUtil.generateToken(claims);

        AdminLoginVO vo = new AdminLoginVO();
        vo.setAdminId(user.getUserId());
        vo.setToken(token);
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());

        log.info("管理员登录成功，userId:{}", user.getUserId());
        return vo;
    }
}