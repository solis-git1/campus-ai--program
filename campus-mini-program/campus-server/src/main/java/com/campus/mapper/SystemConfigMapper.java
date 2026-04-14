package com.campus.mapper;

import com.campus.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SystemConfigMapper {

    List<SystemConfig> listByCategory(@Param("category") String category);

    SystemConfig getByKey(@Param("configKey") String configKey);

    void insert(SystemConfig config);

    void update(SystemConfig config);

    void deleteById(@Param("configId") Long configId);
}
