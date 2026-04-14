package com.campus.mapper;

import com.campus.entity.KnowledgeChunk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KnowledgeChunkMapper {

    KnowledgeChunk getById(@Param("chunkId") Long chunkId);

    List<KnowledgeChunk> queryByDocId(@Param("docId") Long docId);

    void insert(KnowledgeChunk chunk);

    void update(KnowledgeChunk chunk);

    void deleteByDocId(@Param("docId") Long docId);
}
