package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.RequireToken;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.context.UserContext;
import com.tencent.wxcloudrun.context.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息控制器
 * 演示如何使用UserContext获取当前用户信息
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    /**
     * 获取当前用户信息 - 方式1：使用UserContextHolder（推荐）
     */
    @RequireToken
    @GetMapping("/me")
    public ApiResponse getCurrentUser() {
        // 方式1：直接从UserContextHolder获取userId（推荐）
        Integer userId = UserContextHolder.getUserId();

        // 也可以获取完整的UserContext对象
        UserContext context = UserContextHolder.getContext();

        logger.info("获取当前用户信息, userId: {}, requestId: {}", userId, context.getRequestId());

        // 模拟返回用户信息
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("username", "用户" + userId);
        userInfo.put("requestId", context.getRequestId());
        userInfo.put("requestTime", context.getRequestTime());

        return ApiResponse.ok(userInfo);
    }

    /**
     * 获取当前用户信息 - 方式2：从request获取（兼容旧方式）
     */
    @RequireToken
    @GetMapping("/info")
    public ApiResponse getUserInfo(HttpServletRequest request) {
        // 方式2：从request获取userId（兼容旧方式）
        Integer userId = (Integer) request.getAttribute("userId");

        logger.info("获取用户信息, userId: {}", userId);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", userId);
        userInfo.put("username", "用户" + userId);

        return ApiResponse.ok(userInfo);
    }

    /**
     * 获取用户上下文详情 - 演示所有可用信息
     */
    @RequireToken
    @GetMapping("/context")
    public ApiResponse getContextDetails() {
        // 获取完整的UserContext对象
        UserContext context = UserContextHolder.getContext();

        if (context == null) {
            return ApiResponse.error("用户上下文不存在");
        }

        Map<String, Object> contextInfo = new HashMap<>();
        contextInfo.put("userId", context.getUserId());
        contextInfo.put("openId", context.getOpenId());
        contextInfo.put("requestId", context.getRequestId());
        contextInfo.put("requestTime", context.getRequestTime());
        contextInfo.put("timestamp", System.currentTimeMillis());

        logger.info("用户上下文详情: {}", context);

        return ApiResponse.ok(contextInfo);
    }
}