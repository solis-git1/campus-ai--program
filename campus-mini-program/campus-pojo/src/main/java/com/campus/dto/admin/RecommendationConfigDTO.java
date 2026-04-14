package com.campus.dto.admin;

import lombok.Data;

/**
 * 推荐算法配置DTO
 */
@Data
public class RecommendationConfigDTO {
    private String recType; //推荐类型
    private Float weight; //权重
    private Boolean enabled; //是否启用
    private String configJson; //配置JSON
}
