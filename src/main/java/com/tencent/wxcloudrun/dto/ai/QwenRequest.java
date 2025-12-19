package com.tencent.wxcloudrun.dto.ai;

import lombok.Data;

/**
 * 千问AI请求参数
 */
@Data
public class QwenRequest {

    /**
     * 用户问题/提示词
     */
    private String prompt;

    /**
     * 系统角色设定（可选）
     */
    private String systemPrompt;

    /**
     * 温度参数（0.0-2.0，越高越随机）
     */
    private Double temperature = 0.7;

    /**
     * 最大输出token数
     */
    private Integer maxTokens = 2000;
}