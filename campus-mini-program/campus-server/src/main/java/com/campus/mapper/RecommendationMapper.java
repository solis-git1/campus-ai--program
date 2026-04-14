package com.campus.mapper;

import com.campus.entity.Recommendation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@Mapper
public interface RecommendationMapper {

    Recommendation getById(@Param("recId") Long recId);

    List<Recommendation> listByUserId(@Param("userId") Long userId);

    void insert(Recommendation recommendation);

    void update(Recommendation recommendation);

    void deleteById(@Param("recId") Long recId);

    int markClicked(@Param("recId") Long recId, @Param("userId") Long userId);

    List<Recommendation> queryAdminList(@Param("recType") String recType);

    Long countTotal();

    Long countClicked();

    Double clickRate();

    List<Map<String, Object>> statsByType();
}
