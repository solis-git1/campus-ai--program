package com.campus.service;

import com.campus.dto.admin.ClassroomDTO;
import com.campus.entity.Classroom;
import com.campus.entity.ClassroomOccupy;
import com.github.pagehelper.PageInfo;

/**
 * 管理端教室管理业务接口
 */
public interface AdminClassroomService {

    /**
     * 分页查询教室列表
     * @param buildingId 楼栋ID（可选）
     * @param status 状态筛选
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    PageInfo<Classroom> listClassrooms(Long buildingId, String status, Integer page, Integer pageSize);

    /**
     * 根据ID查询教室详情
     * @param classroomId 教室ID
     * @return 教室信息
     */
    Classroom getClassroomDetail(Long classroomId);

    /**
     * 创建教室
     * @param dto 教室参数
     * @return 教室ID
     */
    Long createClassroom(ClassroomDTO dto);

    /**
     * 更新教室
     * @param dto 教室参数
     */
    void updateClassroom(ClassroomDTO dto);

    /**
     * 删除教室
     * @param classroomId 教室ID
     */
    void deleteClassroom(Long classroomId);

    /**
     * 更新教室状态
     * @param classroomId 教室ID
     * @param status 状态
     */
    void updateClassroomStatus(Long classroomId, String status);

    /**
     * 查询教室占用记录
     * @param classroomId 教室ID
     * @param page 页码
     * @param pageSize 分页大小
     * @return 占用记录
     */
    PageInfo<ClassroomOccupy> listOccupies(Long classroomId, Integer page, Integer pageSize);
}
