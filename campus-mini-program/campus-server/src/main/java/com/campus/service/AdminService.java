package com.campus.service;

import com.campus.dto.admin.AdminLoginDTO;
import com.campus.vo.admin.AdminLoginVO;

/**
 * 管理员业务接口
 */
public interface AdminService {

    /**
     * 管理员登录
     * @param dto 登录参数
     * @return 登录结果
     */
    AdminLoginVO login(AdminLoginDTO dto);
}
