package com.campus.controller.user;

import com.campus.entity.Classroom;
import com.campus.result.R;
import com.campus.service.ClassroomService;
import com.campus.vo.ClassroomVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/user/classroom")
@Slf4j
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @GetMapping("/list")
    public R<List<Classroom>> listAll() {
        log.info("查询所有教室");
        List<Classroom> list = classroomService.listAllWithBehavior();
        return R.success(list);
    }

    @GetMapping("/empty")
    public R<List<Classroom>> listEmpty(
            @RequestParam(required = false) LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime
    ) {
        if (date == null) {
            date = LocalDate.now();
        }
        log.info("查询空教室，date:{}, startTime:{}, endTime:{}", date, startTime, endTime);
        List<Classroom> list = classroomService.listEmptyClassroomWithBehavior(date, startTime, endTime);
        return R.success(list);
    }

    /**
     * 多条件筛选空教室（含推荐指数）
     */
    @GetMapping("/empty/filter")
    public R<List<ClassroomVO>> listEmptyWithFilters(
            @RequestParam(required = false) LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime,
            @RequestParam(required = false) Long buildingId,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) Integer hasMedia
    ) {
        log.info("多条件筛选空教室，date:{}, time:{}-{}, buildingId:{}, floor:{}, minCap:{}, media:{}",
                date, startTime, endTime, buildingId, floor, minCapacity, hasMedia);
        List<ClassroomVO> list = classroomService.listEmptyWithFilters(
                date, startTime, endTime, buildingId, floor, minCapacity, hasMedia);
        return R.success(list);
    }

    /**
     * 收藏教室
     */
    @PostMapping("/favorite/{classroomId}")
    public R favorite(@PathVariable Long classroomId) {
        log.info("收藏教室，classroomId:{}", classroomId);
        classroomService.favoriteClassroom(classroomId);
        return R.success();
    }

    /**
     * 取消收藏教室
     */
    @DeleteMapping("/favorite/{classroomId}")
    public R unfavorite(@PathVariable Long classroomId) {
        log.info("取消收藏教室，classroomId:{}", classroomId);
        classroomService.unfavoriteClassroom(classroomId);
        return R.success();
    }

    /**
     * 获取用户收藏的教室ID列表
     */
    @GetMapping("/favorites")
    public R<List<Long>> favorites() {
        log.info("获取收藏的教室列表");
        List<Long> ids = classroomService.getFavoriteClassroomIds();
        return R.success(ids);
    }

    /**
     * 检查是否已收藏教室
     */
    @GetMapping("/check-favorite/{classroomId}")
    public R<Boolean> checkFavorite(@PathVariable Long classroomId) {
        log.info("检查是否收藏教室，classroomId:{}", classroomId);
        boolean favorited = classroomService.isFavorited(classroomId);
        return R.success(favorited);
    }
}
