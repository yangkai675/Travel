package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.ai.QwenRequest;
import com.tencent.wxcloudrun.dto.ai.QwenResponse;

/**
 * 千问AI服务接口
 */
public interface QwenService {

    /**
     * 生成AI回复（同步方式）
     * @param request 请求参数
     * @return AI生成的响应
     */
    QwenResponse generate(QwenRequest request);

    /**
     * 简化调用方式（只传prompt）
     * @param prompt 提示词
     * @return AI生成的文本内容
     */
    String generateText(String prompt);
}