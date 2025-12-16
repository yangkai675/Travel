package com.tencent.wxcloudrun.config;

import lombok.Data;

/**
 * 统一响应格式
 */
@Data
public final class ApiResponse {

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    private ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应（无数据）
     */
    public static ApiResponse ok() {
        return new ApiResponse(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功响应（有数据）
     */
    public static ApiResponse ok(Object data) {
        return new ApiResponse(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 失败响应（使用枚举）
     */
    public static ApiResponse error(ResultCode resultCode) {
        return new ApiResponse(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static ApiResponse error(ResultCode resultCode, String customMessage) {
        return new ApiResponse(resultCode.getCode(), customMessage, null);
    }

    /**
     * 失败响应（完全自定义）
     */
    public static ApiResponse error(Integer code, String message) {
        return new ApiResponse(code, message, null);
    }
}
