package com.campus.mapper;

import com.campus.entity.AiFeedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiFeedbackMapper {

    void insert(AiFeedback feedback);

    AiFeedback getById(@Param("feedbackId") Long feedbackId);

    List<AiFeedback> queryAdminList(@Param("status") String status);

    void update(AiFeedback feedback);

    Long countTotal();
}
