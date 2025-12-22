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
    TOKEN_GENERATE_ERROR(60004, "token生成失败"),

    // AI服务相关返回码 (7xxxx)
    AI_SERVICE_ERROR(70001, "AI服务调用失败"),
    AI_API_KEY_INVALID(70002, "AI API Key无效"),
    AI_QUOTA_EXCEEDED(70003, "AI调用额度已用完"),
    AI_TIMEOUT(70004, "AI服务响应超时"),
    AI_CONTENT_FILTER(70005, "内容被安全过滤"),

    // 旅游攻略相关返回码 (8xxxx)
    TRAVEL_PARAM_INVALID(80001, "旅游参数不合法"),
    TRAVEL_PLAN_GENERATE_FAIL(80002, "旅游攻略生成失败"),
    TRAVEL_CITY_NOT_FOUND(80003, "城市信息未找到"),
    TRAVEL_API_ERROR(80004, "旅游API调用失败"),

    // 收藏相关返回码 (9xxxx)
    COLLECTION_ALREADY_EXISTS(90001, "收藏已存在"),
    COLLECTION_NOT_FOUND(90002, "收藏不存在"),
    COLLECTION_NO_PERMISSION(90003, "无权限访问该收藏"),
    COLLECTION_LIST_EMPTY(90004, "收藏列表为空"),
    COLLECTION_SAVE_ERROR(90005, "收藏保存失败"),
    COLLECTION_TITLE_EMPTY(90006, "收藏标题不能为空"),
    COLLECTION_HISTORY_EXPIRED(90007, "攻略已过期,请重新生成"),
    COLLECTION_HISTORY_NOT_FOUND(90007, "攻略已过期,请重新生成");

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