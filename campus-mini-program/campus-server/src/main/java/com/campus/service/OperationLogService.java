package com.campus.service;

import com.campus.entity.OperationLog;
import com.github.pagehelper.PageInfo;

public interface OperationLogService {

    void recordLog(OperationLog log);

    PageInfo<OperationLog> queryLogs(Long userId, String module, String operation,
                                     String startTime, String endTime,
                                     Integer page, Integer pageSize);

    Long countTotal();
}
