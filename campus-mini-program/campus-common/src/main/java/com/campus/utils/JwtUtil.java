//package com.campus.utils;
//
//import com.campus.constant.Constant;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.Map;
//
///**
// * JWT令牌工具类，用于生成、解析登录令牌
// */
//public class JwtUtil {
//
//    /**
//     * 生成JWT令牌
//     * @param claims 令牌中存储的自定义数据（如用户ID）
//     * @return 生成的JWT令牌字符串
//     */
//    public static String generateToken(Map<String, Object> claims) {
//        SecretKey key = Keys.hmacShaKeyFor(Constant.JWT_SECRET.getBytes());
//        return Jwts.builder()
//                .addClaims(claims)
//                .signWith(key)
//                .setExpiration(new Date(System.currentTimeMillis() + Constant.JWT_EXPIRE))
//                .compact();
//    }
//
//    /**
//     * 解析JWT令牌
//     * @param token 待解析的JWT令牌
//     * @return 令牌中存储的自定义数据
//     */
//    public static Claims parseToken(String token) {
//        SecretKey key = Keys.hmacShaKeyFor(Constant.JWT_SECRET.getBytes());
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}



package com.campus.utils;

import com.campus.constant.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT令牌工具类，用于生成、解析登录令牌
 */
public class JwtUtil {

    /**
     * 生成JWT令牌
     */
    public static String generateToken(Map<String, Object> claims) {
        byte[] keyBytes = Constant.JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        return Jwts.builder()
                .addClaims(claims)
                .signWith(Keys.hmacShaKeyFor(keyBytes), io.jsonwebtoken.SignatureAlgorithm.HS256) // 强制 HS256
                .setExpiration(new Date(System.currentTimeMillis() + Constant.JWT_EXPIRE))
                .compact();
    }

    /**
     * 解析JWT令牌
     */
    public static Claims parseToken(String token) {
        byte[] keyBytes = Constant.JWT_SECRET.getBytes(StandardCharsets.UTF_8);
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}



//package com.campus.utils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//import java.util.Map;
//
//@Component
//public class JwtUtil {
//
//    // 自动从yml读取密钥
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private static long expiration;
//
//    // ===================== 【核心修复：自动生成安全密钥】 =====================
//    private static SecretKey getSigningKey() {
//        return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
//    }
//
//    // 生成Token
//    public static String generateToken(Map<String, Object> claims) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSigningKey()) // 这里不再用你配置的密钥，自动生成安全密钥
//                .compact();
//    }
//
//    // 解析Token
//    public static Claims parseToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    // 验证Token是否过期
//    public boolean isTokenExpired(String token) {
//        return parseToken(token).getExpiration().before(new Date());
//    }
//}