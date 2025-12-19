package com.tencent.wxcloudrun.dto.travel;

import lombok.Data;

/**
 * 旅游攻略生成响应结果
 */
@Data
public class TravelPlanResponse {

    /**
     * AI生成的旅游攻略文本（Markdown格式）
     */
    private String planContent;

    /**
     * 请求ID（用于追踪）
     */
    private String requestId;

    /**
     * token使用情况
     */
    private Integer totalTokens;

    /**
     * 生成是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;
}