package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.context.BaseContext;
import com.campus.dto.task.TaskAddDTO;
import com.campus.exception.BaseException;
import com.campus.entity.Course;
import com.campus.entity.CourseReminder;
import com.campus.entity.Notification;
import com.campus.entity.Task;
import com.campus.entity.TaskReminder;
import com.campus.enumeration.TaskStatusEnum;
import com.campus.mapper.CourseMapper;
import com.campus.mapper.CourseReminderMapper;
import com.campus.mapper.NotificationMapper;
import com.campus.mapper.TaskMapper;
import com.campus.mapper.TaskReminderMapper;
import com.campus.service.TaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskReminderMapper taskReminderMapper;
    private final NotificationMapper notificationMapper;
    private final CourseReminderMapper courseReminderMapper;
    private final CourseMapper courseMapper;

    @Override
    public void addTask(TaskAddDTO dto) {
        Long userId = BaseContext.getCurrentId();

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BaseException(400, "待办标题不能为空");
        }

        if (dto.getDeadline() != null && dto.getDeadline().isBefore(LocalDateTime.now())) {
            throw new BaseException(400, "截止时间不能早于当前时间");
        }

        if (dto.getRemindTime() != null && dto.getDeadline() != null
                && dto.getRemindTime().isAfter(dto.getDeadline())) {
            throw new BaseException(400, "提醒时间不能晚于截止时间");
        }

        Task task = new Task();
        task.setUserId(userId);
        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());
        task.setCategory(dto.getCategory());
        task.setPriority(dto.getPriority());
        task.setDeadline(dto.getDeadline());
        task.setStatus(TaskStatusEnum.UNFINISHED.getDescription());
        task.setCreateTime(LocalDateTime.now());
        taskMapper.insert(task);

        if (dto.getRemindTime() != null) {
            TaskReminder reminder = new TaskReminder();
            reminder.setTaskId(task.getTaskId());
            reminder.setRemindTime(dto.getRemindTime());
            taskReminderMapper.insert(reminder);
        }
    }

    @Override
    public PageInfo<Task> listTask(String status, String category, Integer page, Integer pageSize) {
        Long userId = BaseContext.getCurrentId();
        if (pageSize == null) pageSize = Constant.DEFAULT_PAGE_SIZE;
        if (page == null) page = 1;

        PageHelper.startPage(page, pageSize);
        List<Task> list = taskMapper.list(userId, status, category);
        return new PageInfo<>(list);
    }

    @Override
    public void updateTask(Task task) {
        Long userId = BaseContext.getCurrentId();

        if (task.getTaskId() == null) {
            throw new BaseException(400, "待办id不能为空");
        }

        Task exist = taskMapper.getById(task.getTaskId(), userId);
        if (exist == null) {
            throw new BaseException(404, "待办不存在或你没有权限操作该资源");
        }

        if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDateTime.now())) {
            throw new BaseException(400, "截止时间不能早于当前时间");
        }

        task.setUserId(userId);
        taskMapper.update(task);
    }

    @Override
    public void finishTask(Long taskId) {
        Long userId = BaseContext.getCurrentId();

        if (taskId == null) {
            throw new BaseException(400, "待办id不能为空");
        }

        Task task = taskMapper.getById(taskId, userId);
        if (task == null) {
            throw new BaseException(404, "待办不存在或你没有权限操作该资源");
        }
        if (TaskStatusEnum.FINISHED.getDescription().equals(task.getStatus())) {
            throw new BaseException(409, "该待办已经完成了，无需重复操作");
        }

        task.setStatus(TaskStatusEnum.FINISHED.getDescription());
        task.setCompleteTime(LocalDateTime.now());
        taskMapper.update(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        Long userId = BaseContext.getCurrentId();

        if (taskId == null) {
            throw new BaseException(400, "待办id不能为空");
        }

        Task exist = taskMapper.getById(taskId, userId);
        if (exist == null) {
            throw new BaseException(404, "待办不存在或你没有权限操作该资源");
        }

        taskReminderMapper.deleteByTaskId(taskId);
        taskMapper.delete(taskId, userId);
    }

    @Override
    public void archiveTask(Long taskId) {
        Long userId = BaseContext.getCurrentId();

        if (taskId == null) {
            throw new BaseException(400, "待办id不能为空");
        }

        Task task = taskMapper.getById(taskId, userId);
        if (task == null) {
            throw new BaseException(404, "待办不存在或你没有权限操作该资源");
        }
        if (task.getIsArchived() != null && task.getIsArchived() == 1) {
            throw new BaseException(409, "该待办已归档，无需重复操作");
        }

        task.setIsArchived(1);
        taskMapper.update(task);
    }

    @Override
    public void batchDeleteTasks(List<Long> taskIds) {
        Long userId = BaseContext.getCurrentId();

        if (taskIds == null || taskIds.isEmpty()) {
            throw new BaseException(400, "待删除的待办id列表不能为空");
        }
        if (taskIds.size() > 50) {
            throw new BaseException(400, "单次批量删除数量不能超过50条");
        }

        for (Long taskId : taskIds) {
            Task exist = taskMapper.getById(taskId, userId);
            if (exist != null) {
                taskReminderMapper.deleteByTaskId(taskId);
                taskMapper.delete(taskId, userId);
            }
        }
        log.info("用户{}批量删除{}个待办", userId, taskIds.size());
    }

    @Override
    public void restoreTask(Long taskId) {
        Long userId = BaseContext.getCurrentId();

        if (taskId == null) {
            throw new BaseException(400, "待办id不能为空");
        }

        Task task = taskMapper.getById(taskId, userId);
        if (task == null) {
            throw new BaseException(404, "待办不存在或你没有权限操作该资源");
        }
        if (task.getIsArchived() == null || task.getIsArchived() != 1) {
            throw new BaseException(409, "该待办未归档，无需恢复");
        }

        task.setIsArchived(0);
        taskMapper.update(task);
    }

    @Override
    @Scheduled(cron = "0 * * * * ?")
    public void handleTaskReminder() {
        log.info("开始处理各类提醒...");

        List<TaskReminder> taskReminders = taskReminderMapper.listNeedSend();
        for (TaskReminder reminder : taskReminders) {
            Task task = taskMapper.getById(reminder.getTaskId(), null);
            if (task == null) continue;

            Notification notification = new Notification();
            notification.setUserId(task.getUserId());
            notification.setTitle("待办提醒");
            notification.setContent("你有一个待办任务【" + task.getTitle() + "】即将到期，请及时处理");
            notification.setType("task_reminder");
            notification.setIsRead(0);
            notification.setCreateTime(LocalDateTime.now());
            notificationMapper.insert(notification);

            taskReminderMapper.updateSend(reminder.getReminderId());
        }

        List<CourseReminder> courseReminders = courseReminderMapper.listNeedSend();
        for (CourseReminder reminder : courseReminders) {
            Course course = courseMapper.getById(reminder.getCourseId());
            if (course == null) continue;

            Notification notification = new Notification();
            notification.setUserId(reminder.getUserId());
            notification.setTitle("课程提醒");
            notification.setContent("你有课程【" + course.getCourseName() + "】即将开始，请提前准备");
            notification.setType("course_reminder");
            notification.setIsRead(0);
            notification.setCreateTime(LocalDateTime.now());
            notificationMapper.insert(notification);

            courseReminderMapper.updateSend(reminder.getReminderId());
        }
    }
}