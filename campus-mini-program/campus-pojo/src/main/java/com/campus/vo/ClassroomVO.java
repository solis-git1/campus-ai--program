package com.campus.vo;

import lombok.Data;

/**
 * 教室信息视图对象
 * 
 * <p>该类用于封装教室相关的详细信息，包括教室基本信息、
 * 设施配置和使用状态，主要用于教室查询和推荐功能。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class ClassroomVO {
    /**
     * 教室唯一标识ID
     */
    private Long classroomId;
    
    /**
     * 所属教学楼ID
     */
    private Long buildingId;
    
    /**
     * 教学楼名称
     */
    private String buildingName;
    
    /**
     * 教室房间号
     */
    private String roomNumber;
    
    /**
     * 所在楼层
     */
    private Integer floor;
    
    /**
     * 教室容量（可容纳人数）
     */
    private Integer capacity;
    
    /**
     * 是否配备多媒体设备（0-无，1-有）
     */
    private Integer hasMedia;
    
    /**
     * 教室状态：available(可用)/occupied(占用)/maintenance(维护中)
     */
    private String status;
    
    /**
     * 推荐指数（1-5星），基于使用频率、设施条件等因素计算
     */
    private Integer recommendationScore;
}
