package com.tencent.wxcloudrun.dto.ai;

import lombok.Data;

/**
 * 千问AI响应结果
 */
@Data
public class QwenResponse {

    /**
     * AI生成的文本内容
     */
    private String content;

    /**
     * 请求ID（用于追踪）
     */
    private String requestId;

    /**
     * token使用情况
     */
    private TokenUsage tokenUsage;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;

    @Data
    public static class TokenUsage {
        private Integer inputTokens;   // 输入token数
        private Integer outputTokens;  // 输出token数
        private Integer totalTokens;   // 总token数
    }
}