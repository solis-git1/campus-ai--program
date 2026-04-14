package com.campus.entity;

import lombok.Data;

/**
 * 教室实体类
 * 
 * <p>该类对应数据库中的教室表，用于存储校园内各教室的详细信息，
 * 包括教室位置、容量、设备配置等，支持教室查询和预约功能。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class Classroom {
    /**
     * 教室唯一标识ID，主键
     */
    private Long classroomId;
    
    /**
     * 教学楼ID，关联教学楼表
     */
    private Long buildingId;
    
    /**
     * 教室编号（如：101、201等）
     */
    private String roomNumber;
    
    /**
     * 教室所在楼层
     */
    private Integer floor;
    
    /**
     * 教室容纳人数
     */
    private Integer capacity;
    
    /**
     * 是否多媒体教室：0-否，1-是
     */
    private Integer hasMedia;
    
    /**
     * 教室状态（如：可用、维修中、已占用等）
     */
    private String status;
}