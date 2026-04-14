package com.campus.mapper;

import com.campus.entity.KnowledgeDocument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KnowledgeDocumentMapper {

    KnowledgeDocument getById(@Param("docId") Long docId);

    List<KnowledgeDocument> queryAdminList(@Param("category") String category, @Param("keyword") String keyword);

    void insert(KnowledgeDocument document);

    void update(KnowledgeDocument document);

    void deleteById(@Param("docId") Long docId);
}
