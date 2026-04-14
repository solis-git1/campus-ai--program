package com.campus.utils;

import com.campus.constant.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT令牌工具类，用于生成、解析登录令牌
 */
public class JwtUtil {

    /**
     * 生成JWT令牌
     * @param claims 令牌中存储的自定义数据（如用户ID）
     * @return 生成的JWT令牌字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(Constant.JWT_SECRET.getBytes());
        return Jwts.builder()
                .addClaims(claims)
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + Constant.JWT_EXPIRE))
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token 待解析的JWT令牌
     * @return 令牌中存储的自定义数据
     */
    public static Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Constant.JWT_SECRET.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}