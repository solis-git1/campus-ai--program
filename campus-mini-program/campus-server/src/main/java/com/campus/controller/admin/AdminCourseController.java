package com.campus.controller.admin;

import com.campus.dto.admin.CourseDTO;
import com.campus.entity.Course;
import com.campus.result.R;
import com.campus.service.AdminCourseService;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端课程管理接口
 */
@RestController
@RequestMapping("/admin/course")
@Slf4j
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseService adminCourseService;

    /**
     * 分页查询课程列表
     */
    @GetMapping("/list")
    public R<PageInfo<Course>> list(@RequestParam(required = false) String type,
                                     @RequestParam(required = false) Integer weekday,
                                     @RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询课程列表");
        PageInfo<Course> pageInfo = adminCourseService.listCourses(type, weekday, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 查询课程详情
     */
    @GetMapping("/{courseId}")
    public R<Course> detail(@PathVariable Long courseId) {
        log.info("管理员查看课程详情，courseId:{}", courseId);
        Course course = adminCourseService.getCourseDetail(courseId);
        return R.success(course);
    }

    /**
     * 创建课程
     */
    @PostMapping
    public R<Long> create(@RequestBody @Valid CourseDTO dto) {
        log.info("管理员创建课程");
        Long courseId = adminCourseService.createCourse(dto);
        return R.success(courseId);
    }

    /**
     * 更新课程
     */
    @PutMapping
    public R update(@RequestBody @Valid CourseDTO dto) {
        log.info("管理员更新课程");
        adminCourseService.updateCourse(dto);
        return R.success();
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{courseId}")
    public R delete(@PathVariable Long courseId) {
        log.info("管理员删除课程，courseId:{}", courseId);
        adminCourseService.deleteCourse(courseId);
        return R.success();
    }

    /**
     * 同步教务课表
     */
    @PostMapping("/sync")
    public R<Integer> syncSchedule() {
        log.info("管理员同步教务课表");
        int count = adminCourseService.syncAcademicSchedule();
        return R.success(count);
    }

    /**
     * 获取所有课程类型
     */
    @GetMapping("/types")
    public R<List<String>> listTypes() {
        log.info("获取课程类型列表");
        List<String> types = adminCourseService.listCourseTypes();
        return R.success(types);
    }
}
