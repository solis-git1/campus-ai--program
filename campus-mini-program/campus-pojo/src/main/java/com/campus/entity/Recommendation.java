package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * AI推荐记录
 */
@Data
public class Recommendation {
    private Long recId; //推荐ID
    private Long userId; //用户ID
    private String recType; //推荐类型
    private Long contentId; //内容ID
    private String reason; //推荐原因
    private Float score; //推荐分数
    private Integer isClicked; //是否点击
    private LocalDateTime createTime; //推荐时间
}