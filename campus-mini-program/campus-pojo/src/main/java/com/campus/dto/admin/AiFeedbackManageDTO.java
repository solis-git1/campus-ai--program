package com.campus.dto.admin;

import lombok.Data;

/**
 * AI反馈管理DTO
 */
@Data
public class AiFeedbackManageDTO {
    private Long feedbackId;
    private Integer status; //处理状态
    private String reply; //回复内容
}
