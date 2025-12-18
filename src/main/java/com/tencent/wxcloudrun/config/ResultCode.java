package com.tencent.wxcloudrun.config;

import lombok.Getter;

/**
 * 统一返回码枚举
 */
@Getter
public enum ResultCode {

    // 通用返回码
    SUCCESS(200, "success"),
    PARAM_ERROR(400, "参数错误"),
    SYSTEM_ERROR(500, "系统错误"),

    // 微信相关返回码 (4xxxx)
    WECHAT_API_ERROR(40001, "微信接口调用失败"),
    WECHAT_CODE_INVALID(40002, "微信code无效或已过期"),

    // 用户相关返回码 (5xxxx)
    USER_NOT_FOUND(50001, "用户不存在"),
    USER_SAVE_ERROR(50002, "用户信息保存失败"),

    // Token相关返回码 (6xxxx)
    TOKEN_MISSING(60001, "token不能为空"),
    TOKEN_INVALID(60002, "token无效"),
    TOKEN_EXPIRED(60003, "token已过期"),
    TOKEN_GENERATE_ERROR(60004, "token生成失败");

    /**
     * 返回码
     */
    private final Integer code;

    /**
     * 返回信息
     */
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}