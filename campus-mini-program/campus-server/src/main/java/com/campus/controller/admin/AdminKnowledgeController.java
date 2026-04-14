package com.campus.controller.admin;

import com.campus.dto.admin.KnowledgeDocumentDTO;
import com.campus.entity.KnowledgeChunk;
import com.campus.entity.KnowledgeDocument;
import com.campus.result.R;
import com.campus.service.AdminKnowledgeService;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理端知识库管理接口
 */
@RestController
@RequestMapping("/admin/knowledge")
@Slf4j
@RequiredArgsConstructor
public class AdminKnowledgeController {

    private final AdminKnowledgeService adminKnowledgeService;

    /**
     * 分页查询文档列表
     */
    @GetMapping("/documents/list")
    public R<PageInfo<KnowledgeDocument>> listDocuments(@RequestParam(required = false) String category,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) Integer page,
                                                         @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询文档列表");
        PageInfo<KnowledgeDocument> pageInfo = adminKnowledgeService.listDocuments(category, keyword, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 查询文档详情
     */
    @GetMapping("/documents/{docId}")
    public R<KnowledgeDocument> getDocumentDetail(@PathVariable Long docId) {
        log.info("管理员查看文档详情，docId:{}", docId);
        KnowledgeDocument document = adminKnowledgeService.getDocumentDetail(docId);
        return R.success(document);
    }

    /**
     * 上传文档
     */
    @PostMapping("/documents/upload")
    public R<Long> uploadDocument(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "title", required = false) String title,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "keywords", required = false) String keywords) {
        log.info("管理员上传文档");
        Long docId = adminKnowledgeService.uploadDocument(file, title, category, keywords);
        return R.success(docId);
    }

    /**
     * 保存文档（创建/更新）
     */
    @PostMapping("/documents/save")
    public R<Long> saveDocument(@RequestBody KnowledgeDocumentDTO dto) {
        log.info("管理员保存文档");
        Long docId = adminKnowledgeService.saveDocument(dto);
        return R.success(docId);
    }

    /**
     * 删除文档
     */
    @DeleteMapping("/documents/{docId}")
    public R deleteDocument(@PathVariable Long docId) {
        log.info("管理员删除文档，docId:{}", docId);
        adminKnowledgeService.deleteDocument(docId);
        return R.success();
    }

    /**
     * 查询文档分片列表
     */
    @GetMapping("/documents/{docId}/chunks")
    public R<PageInfo<KnowledgeChunk>> listChunks(@PathVariable Long docId,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer pageSize) {
        log.info("管理员查询文档分片");
        PageInfo<KnowledgeChunk> pageInfo = adminKnowledgeService.listChunks(docId, page, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 重新生成向量
     */
    @PostMapping("/documents/{docId}/regenerate-vectors")
    public R regenerateVectors(@PathVariable Long docId) {
        log.info("重新生成文档向量，docId:{}", docId);
        adminKnowledgeService.regenerateVectors(docId);
        return R.success();
    }
}
