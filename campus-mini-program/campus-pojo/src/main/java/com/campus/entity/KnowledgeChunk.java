package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KnowledgeChunk {
    private Long chunkId;
    private Long docId;
    private String content;
    private String embedding;
    private Integer chunkIndex;
    private LocalDateTime createTime;
}
