package com.tencent.wxcloudrun.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * JWT密钥（生产环境建议从配置文件读取）
     * 注意：密钥长度至少32字节（256位）
     */
    private static final String SECRET_KEY = "your-256-bit-secret-key-change-this-in-production-environment-32bytes";

    /**
     * Token过期时间（7天，单位：毫秒）
     */
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 生成密钥
     */
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成Token
     * @param userId 用户ID
     * @return token字符串
     */
    public String generateToken(Integer userId) {
        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

            String token = Jwts.builder()
                    .setSubject(String.valueOf(userId))  // 设置用户ID
                    .setIssuedAt(now)                     // 签发时间
                    .setExpiration(expiration)            // 过期时间
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)  // 签名算法
                    .compact();

            logger.info("生成token成功, userId: {}, 过期时间: {}", userId, expiration);
            return token;

        } catch (Exception e) {
            logger.error("生成token失败, userId: {}", userId, e);
            throw new RuntimeException("token生成失败", e);
        }
    }

    /**
     * 验证Token并获取用户ID
     * @param token token字符串
     * @return 用户ID
     * @throws Exception token无效或已过期
     */
    public Integer getUserIdFromToken(String token) throws Exception {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userIdStr = claims.getSubject();
            Integer userId = Integer.valueOf(userIdStr);

            logger.debug("token验证成功, userId: {}", userId);
            return userId;

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.warn("token已过期: {}", token);
            throw new Exception("token已过期");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.warn("token签名无效: {}", token);
            throw new Exception("token无效");
        } catch (Exception e) {
            logger.error("token验证失败: {}", token, e);
            throw new Exception("token无效");
        }
    }

    /**
     * 验证Token是否有效
     * @param token token字符串
     * @return true: 有效, false: 无效
     */
    public boolean validateToken(String token) {
        try {
            getUserIdFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}