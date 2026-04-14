package com.campus.controller.admin;

import com.campus.entity.OperationLog;
import com.campus.result.R;
import com.campus.service.OperationLogService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/log")
@Slf4j
@RequiredArgsConstructor
public class AdminLogController {

    private final OperationLogService operationLogService;

    /**
     * 查询操作日志列表
     */
    @GetMapping("/list")
    public R<PageInfo<OperationLog>> list(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        log.info("查询操作日志");
        PageInfo<OperationLog> pageInfo = operationLogService.queryLogs(userId, module, operation, startTime, endTime, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 获取操作日志统计
     */
    @GetMapping("/statistics")
    public R<Long> statistics() {
        log.info("获取操作日志统计");
        Long total = operationLogService.countTotal();
        return R.success(total);
    }
}
