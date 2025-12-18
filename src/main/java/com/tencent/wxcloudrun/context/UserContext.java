package com.tencent.wxcloudrun.context;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户上下文对象
 * 用于存储当前请求的用户信息
 * 每个请求都会创建一个新的UserContext实例
 */
@Data
public class UserContext implements Serializable {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户OpenID（可选，按需使用）
     */
    private String openId;

    /**
     * 请求ID（用于日志追踪）
     */
    private String requestId;

    /**
     * 请求时间戳
     */
    private Long requestTime;

    /**
     * 无参构造函数
     */
    public UserContext() {
        this.requestTime = System.currentTimeMillis();
    }

    /**
     * 构造函数
     * @param userId 用户ID
     */
    public UserContext(Integer userId) {
        this.userId = userId;
        this.requestTime = System.currentTimeMillis();
    }

    /**
     * 构造函数
     * @param userId 用户ID
     * @param requestId 请求ID
     */
    public UserContext(Integer userId, String requestId) {
        this.userId = userId;
        this.requestId = requestId;
        this.requestTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "UserContext{" +
                "userId=" + userId +
                ", openId='" + openId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", requestTime=" + requestTime +
                '}';
    }
}