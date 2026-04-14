package com.campus.mapper;

import com.campus.entity.TaskReminder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskReminderMapper {

    TaskReminder getById(@Param("reminderId") Long reminderId);

    TaskReminder getByTaskId(@Param("taskId") Long taskId);

    List<TaskReminder> listByTaskId(@Param("taskId") Long taskId);

    List<TaskReminder> listNeedSend();

    void insert(TaskReminder reminder);

    void update(TaskReminder reminder);

    void updateSend(@Param("reminderId") Long reminderId);

    void deleteById(@Param("reminderId") Long reminderId);

    void deleteByTaskId(@Param("taskId") Long taskId);
}
