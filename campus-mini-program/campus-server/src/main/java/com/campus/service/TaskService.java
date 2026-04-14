package com.campus.service;

import com.campus.dto.task.TaskAddDTO;
import com.campus.entity.Task;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 待办任务业务接口
 */
public interface TaskService {

    /**
     * 新增待办任务
     * @param dto 待办添加参数
     */
    void addTask(TaskAddDTO dto);

    /**
     * 查询待办列表
     * @param status 任务状态，可选
     * @param category 分类，可选
     * @param page 页码
     * @param pageSize 分页大小
     * @return 待办列表
     */
    PageInfo<Task> listTask(String status, String category, Integer page, Integer pageSize);

    /**
     * 更新待办任务
     * @param task 待办信息
     */
    void updateTask(Task task);

    /**
     * 完成待办任务
     * @param taskId 任务id
     */
    void finishTask(Long taskId);

    /**
     * 删除待办任务
     * @param taskId 任务id
     */
    void deleteTask(Long taskId);

    /**
     * 归档待办任务
     * @param taskId 任务id
     */
    void archiveTask(Long taskId);

    /**
     * 批量删除待办任务
     * @param taskIds 任务id列表
     */
    void batchDeleteTasks(List<Long> taskIds);

    /**
     * 恢复归档的待办任务
     * @param taskId 任务id
     */
    void restoreTask(Long taskId);

    /**
     * 处理待办提醒，定时任务调用
     */
    void handleTaskReminder();
}