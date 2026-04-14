package com.campus.mapper;

import com.campus.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OperationLogMapper {

    void insert(OperationLog log);

    List<OperationLog> queryList(@Param("userId") Long userId,
                                 @Param("module") String module,
                                 @Param("operation") String operation,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime);

    Long countTotal();
}
