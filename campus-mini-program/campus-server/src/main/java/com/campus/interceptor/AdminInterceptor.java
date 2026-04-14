package com.campus.interceptor;

import com.campus.context.BaseContext;
import com.campus.enumeration.RoleEnum;
import com.campus.exception.BaseException;
import com.campus.mapper.UserMapper;
import com.campus.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BaseException(401, "请先登录");
        }

        var user = userMapper.getById(userId);
        if (user == null) {
            throw new BaseException(404, "当前用户不存在，请重新登录");
        }

        if (!RoleEnum.ADMIN.getCode().equals(user.getRole())) {
            log.warn("非管理员用户尝试访问管理端接口，userId:{}, role:{}", userId, user.getRole());
            throw new BaseException(403, "你没有管理员权限，无法访问");
        }

        return true;
    }
}