package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 微信登录响应DTO
 * 注意：不返回 openId 和 unionId，避免敏感信息泄露
 */
@Data
public class WechatLoginResponse {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 是否是新用户
     */
    private Boolean isNewUser;
}