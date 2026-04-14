package com.campus.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * <p>该类对应数据库中的用户表，用于存储系统用户的基本信息，
 * 包括用户认证信息、个人资料、角色权限等核心数据。</p>
 * 
 * @author campus
 * @since 1.0
 */
@Data
public class User {
    /**
     * 用户唯一标识ID，主键
     */
    private Long userId;
    
    /**
     * 用户名，用于登录
     */
    private String username;
    
    /**
     * 密码（加密存储）
     */
    private String password;
    
    /**
     * 用户昵称，用于显示
     */
    private String nickname;
    
    /**
     * 用户头像URL地址
     */
    private String avatar;
    
    /**
     * 用户角色（student-学生，admin-管理员）
     */
    private String role;
    
    /**
     * 用户邮箱
     */
    private String email;
    
    /**
     * 用户手机号
     */
    private String phone;
    
    /**
     * 账号状态（0-正常，1-禁用）
     */
    private Integer status;
    
    /**
     * 账号创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 账号最后更新时间
     */
    private LocalDateTime updateTime;
}