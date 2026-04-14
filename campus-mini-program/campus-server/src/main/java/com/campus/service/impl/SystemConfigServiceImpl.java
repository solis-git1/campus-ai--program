package com.campus.service.impl;

import com.campus.entity.SystemConfig;
import com.campus.exception.BaseException;
import com.campus.mapper.SystemConfigMapper;
import com.campus.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigMapper systemConfigMapper;

    @Override
    public List<SystemConfig> listConfigs(String category) {
        return systemConfigMapper.listByCategory(category);
    }

    @Override
    public SystemConfig getConfig(String configKey) {
        if (configKey == null || configKey.isBlank()) {
            throw new BaseException(400, "配置键不能为空");
        }
        return systemConfigMapper.getByKey(configKey);
    }

    @Override
    public void updateConfig(SystemConfig config) {
        if (config.getConfigId() == null) {
            throw new BaseException(400, "配置id不能为空");
        }
        systemConfigMapper.update(config);
        log.info("更新系统配置：{}", config.getConfigKey());
    }

    @Override
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("serverTime", LocalDateTime.now());
        status.put("status", "running");
        status.put("version", "1.0.0");
        log.info("获取系统运行状态");
        return status;
    }
}
