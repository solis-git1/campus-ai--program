package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户兴趣画像
 */
@Data
public class UserProfile {
    private Long profileId; //画像ID
    private Long userId; //用户ID
    private String interestTags; //兴趣标签
    private String studyPreference; //学习偏好
    private String activeTime; //活跃时间
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime; //更新时间
}