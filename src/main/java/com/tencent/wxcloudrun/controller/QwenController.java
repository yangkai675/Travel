package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.RequireToken;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResultCode;
import com.tencent.wxcloudrun.dto.ai.QwenRequest;
import com.tencent.wxcloudrun.dto.ai.QwenResponse;
import com.tencent.wxcloudrun.service.QwenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 千问AI接口控制器
 */
@RestController
@RequestMapping("/api/ai")
public class QwenController {

    private static final Logger logger = LoggerFactory.getLogger(QwenController.class);

    @Autowired
    private QwenService qwenService;

    /**
     * AI文本生成接口
     * POST /api/ai/generate
     */
    @RequireToken
    @PostMapping("/generate")
    public ApiResponse generate(@RequestBody QwenRequest request) {
        logger.info("收到AI生成请求, prompt长度: {}", request.getPrompt() != null ? request.getPrompt().length() : 0);

        // 参数校验
        if (request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
            return ApiResponse.error(ResultCode.PARAM_ERROR);
        }

        // 调用AI服务
        QwenResponse response = qwenService.generate(request);

        if (response.getSuccess()) {
            return ApiResponse.ok(response);
        } else {
            return ApiResponse.error(ResultCode.AI_SERVICE_ERROR);
        }
    }

    /**
     * 简化版AI生成接口（只需要传prompt）
     * POST /api/ai/chat
     */
    @RequireToken
    @PostMapping("/chat")
    public ApiResponse chat(@RequestParam String message) {
        logger.info("收到AI聊天请求, message: {}", message);

        if (message == null || message.trim().isEmpty()) {
            return ApiResponse.error(ResultCode.PARAM_ERROR);
        }

        String reply = qwenService.generateText(message);
        return ApiResponse.ok(reply);
    }

    /**
     * 健康检查接口（无需token）
     * GET /api/ai/health
     */
    @GetMapping("/health")
    public ApiResponse health() {
        return ApiResponse.ok("Qwen AI服务运行正常");
    }
}