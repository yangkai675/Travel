package com.tencent.wxcloudrun.aspect;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResultCode;
import com.tencent.wxcloudrun.context.UserContext;
import com.tencent.wxcloudrun.context.UserContextHolder;
import com.tencent.wxcloudrun.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Token校验AOP切面
 * 拦截所有带有@RequireToken注解的方法，进行token校验
 */
@Aspect
@Component
public class TokenAspect {

    private static final Logger logger = LoggerFactory.getLogger(TokenAspect.class);

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Token请求头名称
     */
    private static final String TOKEN_HEADER = "Authorization";

    /**
     * Token前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 环绕通知：在方法执行前进行token校验
     */
    @Around("@annotation(com.tencent.wxcloudrun.annotation.RequireToken)")
    public Object validateToken(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // 获取当前请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                logger.error("无法获取当前请求");
                return ApiResponse.error(ResultCode.SYSTEM_ERROR);
            }

            HttpServletRequest request = attributes.getRequest();
            String uri = request.getRequestURI();

            // 1. 从请求头获取token
            String authHeader = request.getHeader(TOKEN_HEADER);
            logger.info("接口 {} 需要token校验, Authorization: {}", uri, authHeader);

            if (authHeader == null || authHeader.isEmpty()) {
                logger.warn("token缺失, 接口: {}", uri);
                return ApiResponse.error(ResultCode.TOKEN_MISSING);
            }

            // 2. 去除Bearer前缀
            String token;
            if (authHeader.startsWith(TOKEN_PREFIX)) {
                token = authHeader.substring(TOKEN_PREFIX.length());
            } else {
                token = authHeader;
            }

            // 3. 验证token
            try {
                Integer userId = jwtUtil.getUserIdFromToken(token);
                logger.info("token验证成功, userId: {}, 接口: {}", userId, uri);

                // 4. 创建用户上下文对象并存入ThreadLocal
                String requestId = UUID.randomUUID().toString().replace("-", "");
                UserContext userContext = new UserContext(userId, requestId);
                UserContextHolder.setContext(userContext);

                logger.debug("用户上下文已设置: {}", userContext);

                // 5. 兼容旧方式：同时存入request（可选，保持向后兼容）
                request.setAttribute("userId", userId);

                // 6. 继续执行目标方法
                return joinPoint.proceed();

            } catch (Exception e) {
                logger.warn("token验证失败, 接口: {}, 错误: {}", uri, e.getMessage());

                // 根据异常类型返回不同的错误码
                if (e.getMessage().contains("过期")) {
                    return ApiResponse.error(ResultCode.TOKEN_EXPIRED);
                } else {
                    return ApiResponse.error(ResultCode.TOKEN_INVALID);
                }
            }

        } finally {
            // ⚠️ 非常重要：请求结束后清理ThreadLocal，避免内存泄漏
            UserContextHolder.clear();
            logger.debug("用户上下文已清理");
        }
    }
}
