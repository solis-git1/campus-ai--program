package com.campus.controller.admin;

import com.campus.dto.admin.ClassroomDTO;
import com.campus.entity.Classroom;
import com.campus.entity.ClassroomOccupy;
import com.campus.result.R;
import com.campus.service.AdminClassroomService;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端教室管理接口
 */
@RestController
@RequestMapping("/admin/classroom")
@Slf4j
@RequiredArgsConstructor
public class AdminClassroomController {

    private final AdminClassroomService adminClassroomService;

    /**
     * 分页查询教室列表
     */
    @GetMapping("/list")
    public R<PageInfo<Classroom>> list(@RequestParam(required = false) Long buildingId,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询教室列表");
        PageInfo<Classroom> pageInfo = adminClassroomService.listClassrooms(buildingId, status, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 查询教室详情
     */
    @GetMapping("/{classroomId}")
    public R<Classroom> detail(@PathVariable Long classroomId) {
        log.info("管理员查看教室详情，classroomId:{}", classroomId);
        Classroom classroom = adminClassroomService.getClassroomDetail(classroomId);
        return R.success(classroom);
    }

    /**
     * 创建教室
     */
    @PostMapping
    public R<Long> create(@RequestBody @Valid ClassroomDTO dto) {
        log.info("管理员创建教室");
        Long classroomId = adminClassroomService.createClassroom(dto);
        return R.success(classroomId);
    }

    /**
     * 更新教室
     */
    @PutMapping
    public R update(@RequestBody @Valid ClassroomDTO dto) {
        log.info("管理员更新教室");
        adminClassroomService.updateClassroom(dto);
        return R.success();
    }

    /**
     * 删除教室
     */
    @DeleteMapping("/{classroomId}")
    public R delete(@PathVariable Long classroomId) {
        log.info("管理员删除教室，classroomId:{}", classroomId);
        adminClassroomService.deleteClassroom(classroomId);
        return R.success();
    }

    /**
     * 更新教室状态
     */
    @PutMapping("/{classroomId}/status")
    public R updateStatus(@PathVariable Long classroomId, @RequestParam String status) {
        log.info("管理员更新教室状态，classroomId:{}, status:{}", classroomId, status);
        adminClassroomService.updateClassroomStatus(classroomId, status);
        return R.success();
    }

    /**
     * 查询教室占用记录
     */
    @GetMapping("/{classroomId}/occupies")
    public R<PageInfo<ClassroomOccupy>> occupies(@PathVariable Long classroomId,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询教室占用记录");
        PageInfo<ClassroomOccupy> pageInfo = adminClassroomService.listOccupies(classroomId, page, pageSize);
        return R.success(pageInfo);
    }
}
