package com.campus.interceptor;

import com.campus.constant.Constant;
import com.campus.context.BaseContext;
import com.campus.exception.BaseException;
import com.campus.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 * 
 * <p>该类实现了用户身份验证功能，拦截所有需要登录的请求，
 * 验证JWT token的有效性，并将用户ID设置到线程上下文中。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 请求预处理方法
     * 
     * <p>该方法在请求处理之前执行，主要功能包括：
     * 1. 检查请求头中是否包含有效的token
     * 2. 解析token获取用户ID
     * 3. 将用户ID设置到线程上下文中
     * 4. 处理token过期、格式错误等异常情况</p>
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器对象
     * @return 是否继续处理请求
     * @throws Exception 如果token验证失败，抛出认证异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(Constant.TOKEN_HEADER);
        if (token == null || token.isEmpty()) {
            log.warn("请求未携带token，IP:{}, URI:{}", request.getRemoteAddr(), request.getRequestURI());
            throw new BaseException(401, "请先登录");
        }

        try {
            Claims claims = JwtUtil.parseToken(token);
            Long userId = (Long) claims.get("userId");
            BaseContext.setCurrentId(userId);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("token已过期，IP:{}", request.getRemoteAddr());
            throw new BaseException(401, "登录已过期，请重新登录");
        } catch (MalformedJwtException e) {
            log.warn("token格式非法，IP:{}", request.getRemoteAddr());
            throw new BaseException(401, "登录凭证无效，请重新登录");
        } catch (Exception e) {
            log.warn("token解析失败，IP:{}", request.getRemoteAddr());
            throw new BaseException(401, "登录状态异常，请重新登录");
        }
    }

    /**
     * 请求完成后的清理方法
     * 
     * <p>该方法在请求处理完成后执行，主要功能是清理线程上下文中的用户ID，
     * 避免内存泄漏问题。</p>
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理器对象
     * @param ex 异常对象，如果有异常发生
     * @throws Exception 如果清理过程中发生异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.remove();
    }
}