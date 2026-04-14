package com.campus.controller.admin;

import com.campus.entity.SystemConfig;
import com.campus.result.R;
import com.campus.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/system")
@Slf4j
@RequiredArgsConstructor
public class AdminSystemController {

    private final SystemConfigService systemConfigService;

    /**
     * 获取系统配置列表
     */
    @GetMapping("/configs")
    public R<List<SystemConfig>> listConfigs(@RequestParam(required = false) String category) {
        log.info("获取系统配置列表，category:{}", category);
        List<SystemConfig> list = systemConfigService.listConfigs(category);
        return R.success(list);
    }

    /**
     * 获取单个配置
     */
    @GetMapping("/config/{configKey}")
    public R<SystemConfig> getConfig(@PathVariable String configKey) {
        log.info("获取系统配置，configKey:{}", configKey);
        SystemConfig config = systemConfigService.getConfig(configKey);
        return R.success(config);
    }

    /**
     * 更新系统配置
     */
    @PutMapping("/config")
    public R updateConfig(@RequestBody SystemConfig config) {
        log.info("更新系统配置，configKey:{}", config.getConfigKey());
        systemConfigService.updateConfig(config);
        return R.success();
    }

    /**
     * 获取系统运行状态
     */
    @GetMapping("/status")
    public R<Map<String, Object>> getSystemStatus() {
        log.info("获取系统运行状态");
        Map<String, Object> status = systemConfigService.getSystemStatus();
        return R.success(status);
    }
}
