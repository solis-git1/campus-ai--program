package com.campus.service;

import com.campus.dto.admin.KnowledgeDocumentDTO;
import com.campus.entity.KnowledgeChunk;
import com.campus.entity.KnowledgeDocument;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理端知识库管理业务接口
 */
public interface AdminKnowledgeService {

    /**
     * 分页查询文档列表
     * @param category 分类
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分页结果
     */
    PageInfo<KnowledgeDocument> listDocuments(String category, String keyword, Integer page, Integer pageSize);

    /**
     * 根据ID查询文档详情
     * @param docId 文档ID
     * @return 文档信息
     */
    KnowledgeDocument getDocumentDetail(Long docId);

    /**
     * 上传文档并处理（分片、向量化）
     * @param file 文件
     * @param title 标题
     * @param category 分类
     * @param keywords 关键词
     * @return 文档ID
     */
    Long uploadDocument(MultipartFile file, String title, String category, String keywords);

    /**
     * 创建/更新文档
     * @param dto 文档参数
     * @return 文档ID
     */
    Long saveDocument(KnowledgeDocumentDTO dto);

    /**
     * 删除文档（包括关联的分片）
     * @param docId 文档ID
     */
    void deleteDocument(Long docId);

    /**
     * 查询文档的分片列表
     * @param docId 文档ID
     * @param page 页码
     * @param pageSize 分页大小
     * @return 分片列表
     */
    PageInfo<KnowledgeChunk> listChunks(Long docId, Integer page, Integer pageSize);

    /**
     * 重新生成文档的向量（为RAG做准备）
     * @param docId 文档ID
     */
    void regenerateVectors(Long docId);
}
