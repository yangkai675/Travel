package com.tencent.wxcloudrun.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 千问AI配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alibaba.dashscope")
public class QwenConfig {

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 模型名称
     */
    private String model = "qwen-max";

    /**
     * 超时时间（毫秒）
     */
    private Integer timeout = 60000;
}