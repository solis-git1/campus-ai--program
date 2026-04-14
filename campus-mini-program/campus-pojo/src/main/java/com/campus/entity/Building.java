package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 教学楼实体类
 * 
 * <p>该类对应数据库中的教学楼表，用于存储校园内各教学楼的基本信息，
 * 包括教学楼名称、所属校区、楼层数等基础数据。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class Building {
    /**
     * 教学楼唯一标识ID，主键
     */
    private Long buildingId;
    
    /**
     * 教学楼名称
     */
    private String buildingName;
    
    /**
     * 所属校区
     */
    private String campus;
    
    /**
     * 教学楼总楼层数
     */
    private Integer floorCount;
    
    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;
}