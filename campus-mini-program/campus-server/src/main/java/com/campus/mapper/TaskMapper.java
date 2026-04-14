package com.campus.mapper;

import com.campus.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskMapper {

    List<Task> list(@Param("userId") Long userId, @Param("status") String status, @Param("category") String category);

    Task getById(@Param("taskId") Long taskId, @Param("userId") Long userId);

    void insert(Task task);

    void update(Task task);

    void delete(@Param("taskId") Long taskId, @Param("userId") Long userId);
}
