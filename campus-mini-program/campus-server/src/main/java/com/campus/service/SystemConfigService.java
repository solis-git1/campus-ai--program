package com.campus.service;

import com.campus.entity.SystemConfig;
import java.util.List;
import java.util.Map;

public interface SystemConfigService {

    List<SystemConfig> listConfigs(String category);

    SystemConfig getConfig(String configKey);

    void updateConfig(SystemConfig config);

    Map<String, Object> getSystemStatus();
}
