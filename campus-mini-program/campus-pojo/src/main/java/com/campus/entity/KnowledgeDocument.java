package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 知识库文档
 */
@Data
public class KnowledgeDocument {
    private Long docId; //文档ID
    private String title; //文档标题
    private String content; //文档内容
    private String source; //来源
    private String fileUrl; //文件地址
    private String keywords; //关键词
    private String category; //分类
    private Integer viewCount; //浏览次数
    private Integer likeCount; //点赞数
    private LocalDateTime createTime; //创建时间
}