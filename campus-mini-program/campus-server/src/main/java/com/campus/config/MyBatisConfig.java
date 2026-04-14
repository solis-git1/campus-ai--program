package com.campus.config;

import com.github.pagehelper.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * MyBatis配置类
 * 
 * <p>该类负责配置MyBatis相关的组件和插件，主要功能包括：
 * 1. 注册分页插件，支持数据库查询结果的分页显示
 * 2. 配置分页合理化参数，提升用户体验</p>
 * 
 * @author campus
 * @since 1.0
 */
@Configuration
public class MyBatisConfig {

    /**
     * 配置分页拦截器
     * 
     * <p>该方法创建并配置PageHelper分页插件，支持数据库查询结果的分页功能，
     * 通过设置reasonable参数实现页码的合理化处理。</p>
     * 
     * @return 配置好的分页拦截器实例
     */
    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        // 分页合理化：页码小于1默认查第一页，大于最大页默认查最后一页
        properties.setProperty("reasonable", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}