package com.campus.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置属性类
 * 
 * <p>该类通过@ConfigurationProperties注解绑定application.yml中的微信配置，
 * 用于管理微信小程序的AppID和AppSecret等认证信息</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatProperties {
    /**
     * 微信小程序的AppID
     */
    private String appid;
    
    /**
     * 微信小程序的AppSecret
     */
    private String secret;
}