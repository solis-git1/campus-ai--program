package com.campus.controller.user;

import com.campus.dto.task.TaskAddDTO;
import com.campus.entity.Task;
import com.campus.result.R;
import com.campus.service.TaskService;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端待办任务相关接口
 */
@RestController
@RequestMapping("/user/task")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * 新增待办任务
     * @param dto 待办添加参数
     * @return 成功结果
     */
    @PostMapping
    public R add(@RequestBody @Valid TaskAddDTO dto) {
        log.info("新增待办：{}", dto);
        taskService.addTask(dto);
        return R.success();
    }

    /**
     * 分页查询待办列表
     * @param status 任务状态，可选
     * @param category 分类，可选
     * @param page 页码，默认1
     * @param pageSize 分页大小，默认10
     * @return 分页结果
     */
    @GetMapping("/list")
    public R<PageInfo<Task>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        log.info("查询待办列表，status:{}, category:{}, page:{}, pageSize:{}", status, category, page, pageSize);
        PageInfo<Task> pageInfo = taskService.listTask(status, category, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 更新待办任务
     * @param task 待办信息
     * @return 成功结果
     */
    @PutMapping
    public R update(@RequestBody Task task) {
        log.info("更新待办：{}", task);
        taskService.updateTask(task);
        return R.success();
    }

    /**
     * 完成待办任务
     * @param taskId 任务id
     * @return 成功结果
     */
    @PostMapping("/finish/{taskId}")
    public R finish(@PathVariable Long taskId) {
        log.info("完成待办：{}", taskId);
        taskService.finishTask(taskId);
        return R.success();
    }

    /**
     * 删除待办任务
     * @param taskId 任务id
     * @return 成功结果
     */
    @DeleteMapping("/{taskId}")
    public R delete(@PathVariable Long taskId) {
        log.info("删除待办：{}", taskId);
        taskService.deleteTask(taskId);
        return R.success();
    }

    /**
     * 归档待办任务
     * @param taskId 任务id
     * @return 成功结果
     */
    @PostMapping("/archive/{taskId}")
    public R archive(@PathVariable Long taskId) {
        log.info("归档待办：{}", taskId);
        taskService.archiveTask(taskId);
        return R.success();
    }

    /**
     * 批量删除待办任务
     * @param taskIds 任务id列表
     * @return 成功结果
     */
    @DeleteMapping("/batch")
    public R batchDelete(@RequestBody List<Long> taskIds) {
        log.info("批量删除待办：{}", taskIds);
        taskService.batchDeleteTasks(taskIds);
        return R.success();
    }

    /**
     * 恢复归档的待办任务
     * @param taskId 任务id
     * @return 成功结果
     */
    @PostMapping("/restore/{taskId}")
    public R restore(@PathVariable Long taskId) {
        log.info("恢复归档待办：{}", taskId);
        taskService.restoreTask(taskId);
        return R.success();
    }
}