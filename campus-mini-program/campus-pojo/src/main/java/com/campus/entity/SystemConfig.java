package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SystemConfig {
    private Long configId;
    private String configKey; //配置键
    private String configValue; //配置值
    private String description; //描述
    private String category; //分类：notification/cache/system
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
