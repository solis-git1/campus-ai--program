package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.CourseDTO;
import com.campus.entity.Course;
import com.campus.enumeration.CourseTypeEnum;
import com.campus.exception.BaseException;
import com.campus.mapper.ClassroomMapper;
import com.campus.mapper.CourseMapper;
import com.campus.service.AdminCourseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminCourseServiceImpl implements AdminCourseService {

    private final CourseMapper courseMapper;
    private final ClassroomMapper classroomMapper;

    @Override
    public PageInfo<Course> listCourses(String type, Integer weekday, Integer page, Integer pageSize) {
        log.info("管理员查询课程列表，type:{}, weekday:{}, page:{}, pageSize:{}", type, weekday, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<Course> list = courseMapper.queryAdminList(null, type, weekday);
        return new PageInfo<>(list);
    }

    @Override
    public Course getCourseDetail(Long courseId) {
        log.info("管理员查看课程详情，courseId:{}", courseId);

        if (courseId == null) {
            throw new BaseException(400, "课程id不能为空");
        }

        Course course = courseMapper.getById(courseId);
        if (course == null) {
            throw new BaseException(404, "课程不存在，请检查课程id是否正确");
        }
        return course;
    }

    @Override
    public Long createCourse(CourseDTO dto) {
        log.info("管理员创建课程：{}", dto.getCourseName());

        if (dto.getCourseName() == null || dto.getCourseName().isBlank()) {
            throw new BaseException(400, "课程名称不能为空");
        }
        if (dto.getWeekday() == null || dto.getWeekday() < 1 || dto.getWeekday() > 7) {
            throw new BaseException(400, "星期几参数不合法，应为1-7之间的整数");
        }
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new BaseException(400, "请输入课程的开始时间和结束时间");
        }
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BaseException(400, "结束时间不能早于开始时间");
        }

        if (dto.getClassroomId() != null && classroomMapper.getById(dto.getClassroomId()) == null) {
            throw new BaseException(404, "指定的教室不存在，请检查教室id是否正确");
        }

        Course course = new Course();
        course.setCourseName(dto.getCourseName());
        course.setTeacher(dto.getTeacher());
        course.setClassroomId(dto.getClassroomId());
        course.setWeekday(dto.getWeekday());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());
        course.setType(dto.getType() != null ? dto.getType() : CourseTypeEnum.REQUIRED.getCode());
        course.setCreateTime(LocalDateTime.now());

        courseMapper.insert(course);
        log.info("课程创建成功，courseId:{}", course.getCourseId());
        return course.getCourseId();
    }

    @Override
    public void updateCourse(CourseDTO dto) {
        log.info("管理员更新课程，courseId:{}", dto.getCourseId());

        if (dto.getCourseId() == null) {
            throw new BaseException(400, "课程id不能为空");
        }

        Course exist = courseMapper.getById(dto.getCourseId());
        if (exist == null) {
            throw new BaseException(404, "要更新的课程不存在，请检查课程id是否正确");
        }

        if (dto.getEndTime() != null && dto.getStartTime() != null
                && dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BaseException(400, "结束时间不能早于开始时间");
        }
        if (dto.getWeekday() != null && (dto.getWeekday() < 1 || dto.getWeekday() > 7)) {
            throw new BaseException(400, "星期几参数不合法，应为1-7之间的整数");
        }

        Course course = new Course();
        course.setCourseId(dto.getCourseId());
        course.setCourseName(dto.getCourseName());
        course.setTeacher(dto.getTeacher());
        course.setClassroomId(dto.getClassroomId());
        course.setWeekday(dto.getWeekday());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());
        if (dto.getType() != null) {
            course.setType(dto.getType());
        }

        courseMapper.update(course);
        log.info("课程更新成功");
    }

    @Override
    public void deleteCourse(Long courseId) {
        log.info("管理员删除课程，courseId:{}", courseId);

        if (courseId == null) {
            throw new BaseException(400, "课程id不能为空");
        }

        Course exist = courseMapper.getById(courseId);
        if (exist == null) {
            throw new BaseException(404, "要删除的课程不存在，请检查课程id是否正确");
        }

        courseMapper.deleteById(courseId);
        log.info("课程删除成功");
    }

    @Override
    public int syncAcademicSchedule() {
        log.info("开始同步教务课表...");
        int count = 0;
        log.info("教务课表同步完成，同步{}门课程", count);
        return count;
    }

    @Override
    public List<String> listCourseTypes() {
        return Arrays.stream(CourseTypeEnum.values())
                .map(CourseTypeEnum::getCode)
                .collect(Collectors.toList());
    }
}