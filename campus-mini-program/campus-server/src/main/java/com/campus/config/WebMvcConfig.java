package com.campus.config;

import com.campus.constant.Constant;
import com.campus.interceptor.AdminInterceptor;
import com.campus.interceptor.LoginInterceptor;
import com.campus.json.JacksonObjectMapper;
import com.campus.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Spring MVC配置类
 * 
 * <p>该类负责配置Spring MVC相关的组件，主要功能包括：
 * 1. 注册登录拦截器和管理员权限拦截器，实现身份验证和权限控制
 * 2. 配置自定义消息转换器，统一处理JSON序列化和反序列化</p>
 * 
 * @author campus
 * @since 1.0
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final UserMapper userMapper;

    /**
     * 构造函数，注入用户Mapper用于权限验证
     * 
     * @param userMapper 用户数据访问对象
     */
    public WebMvcConfig(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 注册拦截器
     * 
     * <p>该方法配置了两个拦截器：
     * 1. 登录拦截器：验证用户token，解析用户身份信息
     * 2. 管理员权限拦截器：验证用户是否具有管理员角色</p>
     * 
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册登录拦截器...");
        // 登录拦截器：验证token，解析用户身份
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/user/login",
                        "/user/user/register",
                        "/admin/admin/login",
                        "/user/activity/list"
                );

        log.info("开始注册管理员权限拦截器...");
        // 管理员权限拦截器：验证管理员角色
        registry.addInterceptor(new AdminInterceptor(userMapper))
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/admin/login");
    }

    /**
     * 扩展消息转换器
     * 
     * <p>该方法注册自定义的Jackson序列化器，用于统一处理Java8时间类型的序列化，
     * 解决前后端时间格式不统一的问题。</p>
     * 
     * @param converters 消息转换器列表
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("注册自定义消息转换器...");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0, converter);
    }
}