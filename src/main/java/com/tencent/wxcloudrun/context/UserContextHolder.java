package com.tencent.wxcloudrun.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户上下文持有者
 * 使用ThreadLocal存储当前请求的用户上下文信息
 *
 * 线程安全：每个线程拥有独立的UserContext副本
 *
 * ⚠️ 重要：使用完毕后必须调用 clear() 方法清理，避免内存泄漏
 */
public class UserContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(UserContextHolder.class);

    /**
     * ThreadLocal存储用户上下文
     * 每个线程独立存储，不会相互干扰
     */
    private static final ThreadLocal<UserContext> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置用户上下文
     * @param context 用户上下文对象
     */
    public static void setContext(UserContext context) {
        if (context == null) {
            logger.warn("尝试设置null的UserContext");
            return;
        }
        CONTEXT_HOLDER.set(context);
        logger.debug("设置用户上下文: {}", context);
    }

    /**
     * 获取用户上下文
     * @return 用户上下文对象，如果不存在返回null
     */
    public static UserContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 获取当前用户ID
     * @return 用户ID，如果上下文不存在返回null
     */
    public static Integer getUserId() {
        UserContext context = getContext();
        return context != null ? context.getUserId() : null;
    }

    /**
     * 获取当前用户OpenID
     * @return 用户OpenID，如果上下文不存在返回null
     */
    public static String getOpenId() {
        UserContext context = getContext();
        return context != null ? context.getOpenId() : null;
    }

    /**
     * 获取当前请求ID
     * @return 请求ID，如果上下文不存在返回null
     */
    public static String getRequestId() {
        UserContext context = getContext();
        return context != null ? context.getRequestId() : null;
    }

    /**
     * 清理用户上下文
     * ⚠️ 非常重要：请求结束后必须调用此方法，避免内存泄漏
     */
    public static void clear() {
        UserContext context = CONTEXT_HOLDER.get();
        if (context != null) {
            logger.debug("清理用户上下文: {}", context);
        }
        CONTEXT_HOLDER.remove();
    }

    /**
     * 判断当前是否有用户上下文
     * @return true: 存在, false: 不存在
     */
    public static boolean hasContext() {
        return CONTEXT_HOLDER.get() != null;
    }
}