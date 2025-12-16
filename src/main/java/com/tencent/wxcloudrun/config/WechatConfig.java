package com.tencent.wxcloudrun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置类
 * 说明：虽然yml中已配置，但此类用于类型安全地访问配置，避免在代码中使用@Value注解
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.miniapp")
public class WechatConfig {
    /**
     * 小程序AppID
     */
    private String appId;

    /**
     * 小程序AppSecret
     */
    private String appSecret;
}