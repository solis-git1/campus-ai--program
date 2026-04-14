package com.campus.dto.admin;

import lombok.Data;

/**
 * 知识库文档创建/更新DTO
 */
@Data
public class KnowledgeDocumentDTO {
    private Long docId;
    private String title;
    private String content;
    private String source;
    private String fileUrl;
    private String keywords;
    private String category;
}
