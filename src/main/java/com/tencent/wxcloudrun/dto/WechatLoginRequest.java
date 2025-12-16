package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 微信登录请求DTO
 */
@Data
public class WechatLoginRequest {
    /**
     * wx.login 获取的 code
     */
    private String code;
}