package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.entity.OperationLog;
import com.campus.mapper.OperationLogMapper;
import com.campus.service.OperationLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    @Override
    public void recordLog(OperationLog log) {
        operationLogMapper.insert(log);
    }

    @Override
    public PageInfo<OperationLog> queryLogs(Long userId, String module, String operation,
                                            String startTime, String endTime,
                                            Integer page, Integer pageSize) {
        if (pageSize == null) pageSize = Constant.DEFAULT_PAGE_SIZE;
        if (page == null) page = 1;

        PageHelper.startPage(page, pageSize);
        List<OperationLog> list = operationLogMapper.queryList(userId, module, operation, startTime, endTime);
        return new PageInfo<>(list);
    }

    @Override
    public Long countTotal() {
        return operationLogMapper.countTotal();
    }
}
