package com.campus.vo.admin;

import lombok.Data;

/**
 * 管理员登录返回视图对象
 * 
 * <p>该类用于封装管理员登录成功后返回的数据，
 * 包括管理员基本信息和安全认证token。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class AdminLoginVO {
    /**
     * 管理员唯一标识ID
     */
    private Long adminId;
    
    /**
     * JWT认证令牌，用于后续接口的身份验证
     */
    private String token;
    
    /**
     * 管理员登录用户名
     */
    private String username;
    
    /**
     * 管理员昵称，用于界面显示
     */
    private String nickname;
}
