package com.campus.service.impl;

import com.campus.constant.Constant;
import com.campus.dto.admin.KnowledgeDocumentDTO;
import com.campus.entity.KnowledgeChunk;
import com.campus.entity.KnowledgeDocument;
import com.campus.exception.BaseException;
import com.campus.mapper.KnowledgeChunkMapper;
import com.campus.mapper.KnowledgeDocumentMapper;
import com.campus.service.AdminKnowledgeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 管理端知识库管理业务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminKnowledgeServiceImpl implements AdminKnowledgeService {

    private final KnowledgeDocumentMapper documentMapper;
    private final KnowledgeChunkMapper chunkMapper;

    /**
     * 分页查询文档列表
     */
    @Override
    public PageInfo<KnowledgeDocument> listDocuments(String category, String keyword, Integer page, Integer pageSize) {
        log.info("管理员查询文档列表，category:{}, keyword:{}, page:{}, pageSize:{}", category, keyword, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<KnowledgeDocument> list = documentMapper.queryAdminList(category, keyword);
        return new PageInfo<>(list);
    }

    /**
     * 查询文档详情
     */
    @Override
    public KnowledgeDocument getDocumentDetail(Long docId) {
        log.info("管理员查看文档详情，docId:{}", docId);
        KnowledgeDocument document = documentMapper.getById(docId);
        if (document == null) {
            throw new BaseException("文档不存在");
        }
        return document;
    }

    /**
     * 上传文档并处理
     */
    @Override
    public Long uploadDocument(MultipartFile file, String title, String category, String keywords) {
        log.info("管理员上传文档：{}", title);
        if (file.isEmpty()) {
            throw new BaseException("文件不能为空");
        }

        try {
            // 1. 保存文件信息到数据库
            String originalFilename = file.getOriginalFilename();
            String fileUrl = "/uploads/knowledge/" + UUID.randomUUID() + "_" + originalFilename;

            KnowledgeDocument document = new KnowledgeDocument();
            document.setTitle(title != null ? title : originalFilename);
            document.setSource(originalFilename);
            document.setFileUrl(fileUrl);
            document.setCategory(category);
            document.setKeywords(keywords);
            document.setViewCount(0);
            document.setLikeCount(0);
            document.setCreateTime(LocalDateTime.now());

            // 2. 读取文件内容（简化实现）
            byte[] bytes = file.getBytes();
            String content = new String(bytes, "UTF-8");
            document.setContent(content.substring(0, Math.min(content.length(), 10000))); // 截取前10000字符

            documentMapper.insert(document);
            log.info("文档上传成功，docId:{}", document.getDocId());

            // 3. 文档分片处理
            splitDocumentIntoChunks(document.getDocId(), content);

            return document.getDocId();
        } catch (IOException e) {
            log.error("文件读取失败", e);
            throw new BaseException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 创建/更新文档
     */
    @Override
    public Long saveDocument(KnowledgeDocumentDTO dto) {
        log.info("管理员保存文档：{}", dto.getTitle());
        if (dto.getDocId() != null) {
            // 更新
            KnowledgeDocument exist = documentMapper.getById(dto.getDocId());
            if (exist == null) {
                throw new BaseException("文档不存在");
            }
            KnowledgeDocument document = new KnowledgeDocument();
            document.setDocId(dto.getDocId());
            document.setTitle(dto.getTitle());
            document.setContent(dto.getContent());
            document.setSource(dto.getSource());
            document.setKeywords(dto.getKeywords());
            document.setCategory(dto.getCategory());
            documentMapper.update(document);
            return dto.getDocId();
        } else {
            // 新增
            KnowledgeDocument document = new KnowledgeDocument();
            document.setTitle(dto.getTitle());
            document.setContent(dto.getContent());
            document.setSource(dto.getSource());
            document.setFileUrl(dto.getFileUrl());
            document.setKeywords(dto.getKeywords());
            document.setCategory(dto.getCategory());
            document.setViewCount(0);
            document.setLikeCount(0);
            document.setCreateTime(LocalDateTime.now());
            documentMapper.insert(document);
            return document.getDocId();
        }
    }

    /**
     * 删除文档
     */
    @Override
    public void deleteDocument(Long docId) {
        log.info("管理员删除文档，docId:{}", docId);
        KnowledgeDocument exist = documentMapper.getById(docId);
        if (exist == null) {
            throw new BaseException("文档不存在");
        }

        // 先删除关联的分片
        chunkMapper.deleteByDocId(docId);
        documentMapper.deleteById(docId);
        log.info("文档删除成功");
    }

    /**
     * 查询文档分片列表
     */
    @Override
    public PageInfo<KnowledgeChunk> listChunks(Long docId, Integer page, Integer pageSize) {
        log.info("管理员查询文档分片，docId:{}, page:{}, pageSize:{}", docId, page, pageSize);
        PageHelper.startPage(page != null ? page : 1, pageSize != null ? pageSize : Constant.DEFAULT_PAGE_SIZE);
        List<KnowledgeChunk> list = chunkMapper.queryByDocId(docId);
        return new PageInfo<>(list);
    }

    /**
     * 重新生成向量
     */
    @Override
    public void regenerateVectors(Long docId) {
        log.info("重新生成文档向量，docId:{}", docId);
        KnowledgeDocument document = documentMapper.getById(docId);
        if (document == null) {
            throw new BaseException("文档不存在");
        }

        List<KnowledgeChunk> chunks = chunkMapper.queryByDocId(docId);
        for (KnowledgeChunk chunk : chunks) {
            // 这里应该调用向量化模型生成embedding
            // 当前为模拟实现
            chunk.setEmbedding(generateMockVector(chunk.getContent()));
            chunkMapper.update(chunk);
        }
        log.info("向量重新生成完成");
    }

    /**
     * 文档分片处理
     */
    private void splitDocumentIntoChunks(Long docId, String content) {
        int chunkSize = 500; // 每个分片的字符数
        int overlap = 50; // 重叠字符数

        List<KnowledgeChunk> chunks = new ArrayList<>();
        int start = 0;
        int index = 1;

        while (start < content.length()) {
            int end = Math.min(start + chunkSize, content.length());
            String chunkContent = content.substring(start, end);

            KnowledgeChunk chunk = new KnowledgeChunk();
            chunk.setDocId(docId);
            chunk.setChunkIndex(index++);
            chunk.setContent(chunkContent);
            chunk.setEmbedding(generateMockVector(chunkContent)); // 生成模拟向量

            chunks.add(chunk);
            start += (chunkSize - overlap);
        }

        for (KnowledgeChunk chunk : chunks) {
            chunkMapper.insert(chunk);
        }
        log.info("文档分片完成，共{}个分片", chunks.size());
    }

    /**
     * 生成模拟向量（实际项目中应使用真实的embedding模型）
     */
    private String generateMockVector(String text) {
        double[] vector = new double[128];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = Math.random();
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i < vector.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
