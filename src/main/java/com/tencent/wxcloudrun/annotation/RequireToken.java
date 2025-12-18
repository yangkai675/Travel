package com.tencent.wxcloudrun.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Token校验注解
 * 在需要校验token的接口方法上添加此注解
 *
 * 使用示例:
 * @RequireToken
 * @GetMapping("/api/user/info")
 * public ApiResponse getUserInfo() {
 *     // 业务逻辑
 * }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireToken {
}