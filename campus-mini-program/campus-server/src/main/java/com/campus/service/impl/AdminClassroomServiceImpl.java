package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.ClassroomDTO;
import com.campus.entity.Classroom;
import com.campus.entity.ClassroomOccupy;
import com.campus.exception.BaseException;
import com.campus.mapper.BuildingMapper;
import com.campus.mapper.ClassroomMapper;
import com.campus.mapper.ClassroomOccupyMapper;
import com.campus.service.AdminClassroomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminClassroomServiceImpl implements AdminClassroomService {

    private final ClassroomMapper classroomMapper;
    private final ClassroomOccupyMapper occupyMapper;
    private final BuildingMapper buildingMapper;

    @Override
    public PageInfo<Classroom> listClassrooms(Long buildingId, String status, Integer page, Integer pageSize) {
        log.info("管理员查询教室列表，buildingId:{}, status:{}, page:{}, pageSize:{}", buildingId, status, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<Classroom> list = classroomMapper.queryAdminList(buildingId, status);
        return new PageInfo<>(list);
    }

    @Override
    public Classroom getClassroomDetail(Long classroomId) {
        log.info("管理员查看教室详情，classroomId:{}", classroomId);

        if (classroomId == null) {
            throw new BaseException(400, "教室id不能为空");
        }

        Classroom classroom = classroomMapper.getById(classroomId);
        if (classroom == null) {
            throw new BaseException(404, "教室不存在，请检查教室id是否正确");
        }
        return classroom;
    }

    @Override
    public Long createClassroom(ClassroomDTO dto) {
        log.info("管理员创建教室：{}", dto.getRoomNumber());

        if (dto.getRoomNumber() == null || dto.getRoomNumber().isBlank()) {
            throw new BaseException(400, "教室编号不能为空");
        }
        if (dto.getBuildingId() == null) {
            throw new BaseException(400, "教学楼id不能为空，请选择所属教学楼");
        }
        if (buildingMapper.getById(dto.getBuildingId()) == null) {
            throw new BaseException(404, "指定的教学楼不存在，请检查楼栋id是否正确");
        }

        Classroom exist = classroomMapper.getByRoomNumberAndBuilding(dto.getRoomNumber(), dto.getBuildingId());
        if (exist != null) {
            throw new BaseException(409, "该教学楼下已存在相同编号的教室「" + dto.getRoomNumber() + "」，请更换编号");
        }

        Classroom classroom = new Classroom();
        classroom.setBuildingId(dto.getBuildingId());
        classroom.setRoomNumber(dto.getRoomNumber());
        classroom.setFloor(dto.getFloor());
        classroom.setCapacity(dto.getCapacity());
        classroom.setHasMedia(dto.getHasMedia());
        classroom.setStatus(dto.getStatus() != null ? dto.getStatus() : "available");

        classroomMapper.insert(classroom);
        log.info("教室创建成功，classroomId:{}", classroom.getClassroomId());
        return classroom.getClassroomId();
    }

    @Override
    public void updateClassroom(ClassroomDTO dto) {
        log.info("管理员更新教室，classroomId:{}", dto.getClassroomId());

        if (dto.getClassroomId() == null) {
            throw new BaseException(400, "教室id不能为空");
        }

        Classroom exist = classroomMapper.getById(dto.getClassroomId());
        if (exist == null) {
            throw new BaseException(404, "要更新的教室不存在，请检查教室id是否正确");
        }

        Classroom classroom = new Classroom();
        classroom.setClassroomId(dto.getClassroomId());
        classroom.setBuildingId(dto.getBuildingId());
        classroom.setRoomNumber(dto.getRoomNumber());
        classroom.setFloor(dto.getFloor());
        classroom.setCapacity(dto.getCapacity());
        classroom.setHasMedia(dto.getHasMedia());
        if (dto.getStatus() != null) {
            classroom.setStatus(dto.getStatus());
        }

        classroomMapper.update(classroom);
        log.info("教室更新成功");
    }

    @Override
    public void deleteClassroom(Long classroomId) {
        log.info("管理员删除教室，classroomId:{}", classroomId);

        if (classroomId == null) {
            throw new BaseException(400, "教室id不能为空");
        }

        Classroom exist = classroomMapper.getById(classroomId);
        if (exist == null) {
            throw new BaseException(404, "要删除的教室不存在，请检查教室id是否正确");
        }

        Long courseCount = classroomMapper.countCoursesByClassroom(classroomId);
        if (courseCount > 0) {
            throw new BaseException(409, "该教室有" + courseCount + "门课程在使用中，无法删除。如需删除请先移除关联课程");
        }

        occupyMapper.deleteByClassroomId(classroomId);
        classroomMapper.deleteById(classroomId);
        log.info("教室删除成功");
    }

    @Override
    public void updateClassroomStatus(Long classroomId, String status) {
        log.info("管理员更新教室状态，classroomId:{}, status:{}", classroomId, status);

        if (classroomId == null) {
            throw new BaseException(400, "教室id不能为空");
        }

        Classroom exist = classroomMapper.getById(classroomId);
        if (exist == null) {
            throw new BaseException(404, "教室不存在，请检查教室id是否正确");
        }

        Classroom classroom = new Classroom();
        classroom.setClassroomId(classroomId);
        classroom.setStatus(status);
        classroomMapper.update(classroom);
        log.info("教室状态更新成功");
    }

    @Override
    public PageInfo<ClassroomOccupy> listOccupies(Long classroomId, Integer page, Integer pageSize) {
        log.info("管理员查询教室占用记录，classroomId:{}, page:{}, pageSize:{}", classroomId, page, pageSize);

        if (classroomId == null) {
            throw new BaseException(400, "教室id不能为空");
        }

        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<ClassroomOccupy> list = occupyMapper.queryByClassroomId(classroomId);
        return new PageInfo<>(list);
    }
}