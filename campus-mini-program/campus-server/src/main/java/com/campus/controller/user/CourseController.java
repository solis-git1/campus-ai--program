package com.campus.controller.user;

import com.campus.result.R;
import com.campus.service.CourseService;
import com.campus.vo.CourseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户端课程/课表相关接口
 */
@RestController
@RequestMapping("/user/course")
@Slf4j
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * 获取我的课表（含状态标记：current/completed）
     * @return 课表列表
     */
    @GetMapping("/my")
    public R<List<CourseVO>> getMyCourse() {
        log.info("获取我的课表");
        List<CourseVO> list = courseService.getMyCourse();
        return R.success(list);
    }

    /**
     * 获取课程详情
     * @param courseId 课程id
     * @return 课程详情（含状态标记）
     */
    @GetMapping("/{courseId}")
    public R<CourseVO> getDetail(@PathVariable Long courseId) {
        log.info("获取课程详情，courseId:{}", courseId);
        CourseVO vo = courseService.getCourseDetail(courseId);
        return R.success(vo);
    }

    /**
     * 添加课程到我的课表
     * @param courseId 课程id
     * @param note 备注
     * @return 成功结果
     */
    @PostMapping("/add/{courseId}")
    public R addToMy(@PathVariable Long courseId, @RequestParam(required = false) String note) {
        log.info("添加课程到我的课表，courseId:{}, note:{}", courseId, note);
        courseService.addCourseToMy(courseId, note);
        return R.success();
    }

    /**
     * 从我的课表删除课程
     * @param courseId 课程id
     * @return 成功结果
     */
    @DeleteMapping("/remove/{courseId}")
    public R removeFromMy(@PathVariable Long courseId) {
        log.info("从我的课表删除课程，courseId:{}", courseId);
        courseService.removeCourseFromMy(courseId);
        return R.success();
    }

    /**
     * 设置课程提醒
     * @param courseId 课程id
     * @param remindTime 提醒时间
     * @param repeatType 重复类型，如"week"表示每周重复
     * @return 成功结果
     */
    @PostMapping("/reminder/{courseId}")
    public R setReminder(
            @PathVariable Long courseId,
            // 👇 加上这个注解即可
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime remindTime,
            @RequestParam(required = false, defaultValue = "week") String repeatType
    ) {
        log.info("设置课程提醒，courseId:{}, remindTime:{}, repeatType:{}", courseId, remindTime, repeatType);
        courseService.setCourseReminder(courseId, remindTime, repeatType);
        return R.success();
    }
}