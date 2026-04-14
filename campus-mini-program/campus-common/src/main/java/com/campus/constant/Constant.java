package com.campus.constant;

/**
 * 系统通用常量
 */
public class Constant {
    /**
     * JWT令牌过期时间，7天
     */
    public static final long JWT_EXPIRE = 7 * 24 * 60 * 60 * 1000L;
    /**
     * JWT签名密钥
     */
    public static final String JWT_SECRET = "campus_assistant_secret_2026";
    /**
     * 请求头中Token的名称
     */
    public static final String TOKEN_HEADER = "token";
    /**
     * 默认分页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 用户默认初始密码
     */
    public static final String DEFAULT_PASSWORD = "123456";
    /**
     * 超级管理员ID
     */
    public static final Long ADMIN_USER_ID = 1L;
    /**
     * 逻辑删除标识：未删除
     */
    public static final Integer NOT_DELETED = 0;
    /**
     * 逻辑删除标识：已删除
     */
    public static final Integer DELETED = 1;
}