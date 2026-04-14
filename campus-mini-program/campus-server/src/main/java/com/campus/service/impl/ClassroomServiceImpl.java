package com.campus.service.impl;

import com.campus.context.BaseContext;
import com.campus.entity.Classroom;
import com.campus.entity.ClassroomFavorite;
import com.campus.entity.UserBehavior;
import com.campus.exception.BaseException;
import com.campus.mapper.ClassroomFavoriteMapper;
import com.campus.mapper.ClassroomMapper;
import com.campus.mapper.UserBehaviorMapper;
import com.campus.service.ClassroomService;
import com.campus.vo.ClassroomVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomMapper classroomMapper;
    private final UserBehaviorMapper userBehaviorMapper;
    private final ClassroomFavoriteMapper favoriteMapper;

    @Override
    public List<Classroom> listAll() {
        return classroomMapper.listAll();
    }

    @Override
    public List<Classroom> listEmptyClassroom(LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new BaseException(400, "开始时间和结束时间不能为空");
        }
        if (endTime.isBefore(startTime)) {
            throw new BaseException(400, "结束时间不能早于开始时间");
        }
        if (endTime.equals(startTime)) {
            throw new BaseException(400, "结束时间不能等于开始时间");
        }
        if (date != null && date.isBefore(LocalDate.now())) {
            throw new BaseException(400, "查询日期不能早于今天");
        }

        return classroomMapper.listEmptyClassroom(date, startTime, endTime, date.getDayOfWeek().getValue());
    }

    @Override
    public List<Classroom> listAllWithBehavior() {
        Long userId = BaseContext.getCurrentId();
        recordBehavior(userId, "classroom_list", null);
        return classroomMapper.listAll();
    }

    @Override
    public List<Classroom> listEmptyClassroomWithBehavior(LocalDate date, LocalTime startTime, LocalTime endTime) {
        Long userId = BaseContext.getCurrentId();

        if (startTime == null || endTime == null) {
            throw new BaseException(400, "开始时间和结束时间不能为空");
        }
        if (endTime.isBefore(startTime)) {
            throw new BaseException(400, "结束时间不能早于开始时间");
        }
        if (endTime.equals(startTime)) {
            throw new BaseException(400, "结束时间不能等于开始时间");
        }
        if (date != null && date.isBefore(LocalDate.now())) {
            throw new BaseException(400, "查询日期不能早于今天");
        }

        recordBehavior(userId, "empty_classroom_query", date + " " + startTime + "-" + endTime);
        return classroomMapper.listEmptyClassroom(date, startTime, endTime, date.getDayOfWeek().getValue());
    }

    @Override
    public List<ClassroomVO> listEmptyWithFilters(LocalDate date, LocalTime startTime, LocalTime endTime,
                                                   Long buildingId, Integer floor, Integer minCapacity, Integer hasMedia) {
        Long userId = BaseContext.getCurrentId();

        if (startTime == null || endTime == null) {
            throw new BaseException(400, "开始时间和结束时间不能为空");
        }
        if (endTime.isBefore(startTime)) {
            throw new BaseException(400, "结束时间不能早于开始时间");
        }
        if (endTime.equals(startTime)) {
            throw new BaseException(400, "结束时间不能等于开始时间");
        }
        if (date == null) {
            date = LocalDate.now();
        }
        if (date.isBefore(LocalDate.now())) {
            throw new BaseException(400, "查询日期不能早于今天");
        }

        recordBehavior(userId, "empty_classroom_filter", String.format("%s %s-%s b:%s f:%s cap:%s media:%s",
                date, startTime, endTime, buildingId, floor, minCapacity, hasMedia));

        List<ClassroomVO> list = classroomMapper.listEmptyWithFilters(
                date, startTime, endTime, date.getDayOfWeek().getValue(), buildingId, floor, minCapacity, hasMedia);

        list.forEach(vo -> {
            int score = calculateRecommendationScore(vo.getCapacity(), vo.getHasMedia());
            vo.setRecommendationScore(score);
        });

        return list.stream()
                .sorted((a, b) -> b.getRecommendationScore() - a.getRecommendationScore())
                .collect(Collectors.toList());
    }

    private int calculateRecommendationScore(Integer capacity, Integer hasMedia) {
        int score = 3;
        if (capacity != null) {
            if (capacity >= 100) score += 1;
            else if (capacity >= 50) score += 0;
            else score -= 1;
        }
        if (hasMedia != null && hasMedia == 1) {
            score += 1;
        }
        return Math.max(1, Math.min(5, score));
    }

    @Override
    public void favoriteClassroom(Long classroomId) {
        Long userId = BaseContext.getCurrentId();

        if (classroomId == null) {
            throw new BaseException(400, "教室id不能为空");
        }

        Classroom classroom = classroomMapper.getById(classroomId);
        if (classroom == null) {
            throw new BaseException(404, "教室不存在");
        }

        boolean exists = favoriteMapper.existsByUserAndClassroom(userId, classroomId);
        if (exists) {
            throw new BaseException(409, "你已经收藏过该教室了");
        }

        ClassroomFavorite favorite = new ClassroomFavorite();
        favorite.setUserId(userId);
        favorite.setClassroomId(classroomId);
        favorite.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite);

        log.info("用户{}收藏教室{}", userId, classroomId);
    }

    @Override
    public void unfavoriteClassroom(Long classroomId) {
        Long userId = BaseContext.getCurrentId();

        if (classroomId == null) {
            throw new BaseException(400, "教室id不能为空");
        }

        favoriteMapper.delete(userId, classroomId);
        log.info("用户{}取消收藏教室{}", userId, classroomId);
    }

    @Override
    public List<Long> getFavoriteClassroomIds() {
        Long userId = BaseContext.getCurrentId();
        List<ClassroomFavorite> favorites = favoriteMapper.listByUserId(userId);
        return favorites.stream()
                .map(ClassroomFavorite::getClassroomId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorited(Long classroomId) {
        Long userId = BaseContext.getCurrentId();
        return favoriteMapper.existsByUserAndClassroom(userId, classroomId);
    }

    private void recordBehavior(Long userId, String behaviorType, String content) {
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setBehaviorType(behaviorType);
        behavior.setContent(content);
        behavior.setCreateTime(LocalDateTime.now());
        userBehaviorMapper.insert(behavior);
    }
}