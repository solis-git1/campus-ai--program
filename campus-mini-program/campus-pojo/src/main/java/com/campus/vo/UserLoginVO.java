package com.campus.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户登录返回视图对象
 * 
 * <p>该类用于封装用户登录成功后返回的数据，
 * 包括用户基本信息和安全认证token。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class UserLoginVO implements Serializable {
    /**
     * 用户唯一标识ID
     */
    private Long userId;
    
    /**
     * JWT认证令牌，用于后续接口的身份验证
     */
    private String token;
    
    /**
     * 用户昵称，用于界面显示
     */
    private String nickname;
    
    /**
     * 用户头像URL地址
     */
    private String avatar;
}