package com.campus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 校园助手小程序后端应用主类
 * 
 * <p>该类是Spring Boot应用的启动入口，配置了应用的基本设置，
 * 包括MyBatis映射器扫描、定时任务启用等核心功能。</p>
 * 
 * @author campus
 * @since 1.0
 */
@SpringBootApplication
@MapperScan("com.campus.mapper")
@EnableScheduling
public class CampusApplication {
    
    /**
     * 应用主入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(CampusApplication.class, args);
    }
}