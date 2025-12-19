package com.tencent.wxcloudrun.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.tencent.wxcloudrun.config.QwenConfig;
import com.tencent.wxcloudrun.dto.ai.QwenRequest;
import com.tencent.wxcloudrun.dto.ai.QwenResponse;
import com.tencent.wxcloudrun.service.QwenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 千问AI服务实现类
 */
@Service
public class QwenServiceImpl implements QwenService {

    private static final Logger logger = LoggerFactory.getLogger(QwenServiceImpl.class);

    @Autowired
    private QwenConfig qwenConfig;

    @Override
    public QwenResponse generate(QwenRequest request) {
        QwenResponse response = new QwenResponse();

        try {
            // 1. 构建Generation对象
            Generation generation = new Generation();

            // 2. 构建请求参数
            GenerationParam.GenerationParamBuilder<?, ?> builder = GenerationParam.builder()
                    .apiKey(qwenConfig.getApiKey())
                    .model(qwenConfig.getModel())
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE);

            // 3. 添加系统提示（如果有）
            if (request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()) {
                builder.messages(Arrays.asList(
                    systemMessage(request.getSystemPrompt()),
                    userMessage(request.getPrompt())
                ));
            } else {
                builder.messages(Arrays.asList(
                    userMessage(request.getPrompt())
                ));
            }

            // 4. 设置可选参数
            if (request.getTemperature() != null) {
                builder.temperature(request.getTemperature().floatValue());
            }
            if (request.getMaxTokens() != null) {
                builder.maxTokens(request.getMaxTokens());
            }

            GenerationParam param = builder.build();

            // 5. 调用API
            logger.info("调用千问API, model: {}, prompt长度: {}",
                qwenConfig.getModel(), request.getPrompt().length());

            GenerationResult result = generation.call(param);

            // 6. 解析响应
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();

            response.setSuccess(true);
            response.setContent(content);
            response.setRequestId(result.getRequestId());

            // 7. 设置token使用情况
            if (result.getUsage() != null) {
                QwenResponse.TokenUsage tokenUsage = new QwenResponse.TokenUsage();
                tokenUsage.setInputTokens(result.getUsage().getInputTokens());
                tokenUsage.setOutputTokens(result.getUsage().getOutputTokens());
                tokenUsage.setTotalTokens(result.getUsage().getTotalTokens());
                response.setTokenUsage(tokenUsage);

                logger.info("千问API调用成功, requestId: {}, tokens: {}",
                    result.getRequestId(), tokenUsage.getTotalTokens());
            }

        } catch (NoApiKeyException e) {
            logger.error("API Key未配置或无效", e);
            response.setSuccess(false);
            response.setErrorMessage("API Key配置错误");
        } catch (InputRequiredException e) {
            logger.error("请求参数缺失", e);
            response.setSuccess(false);
            response.setErrorMessage("请求参数不完整");
        } catch (ApiException e) {
            logger.error("千问API调用失败, code: {}, message: {}",
                e.getStatus().getStatusCode(), e.getMessage(), e);
            response.setSuccess(false);
            response.setErrorMessage("AI服务调用失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("千问服务异常", e);
            response.setSuccess(false);
            response.setErrorMessage("系统错误: " + e.getMessage());
        }

        return response;
    }

    @Override
    public String generateText(String prompt) {
        QwenRequest request = new QwenRequest();
        request.setPrompt(prompt);

        QwenResponse response = generate(request);

        if (response.getSuccess()) {
            return response.getContent();
        } else {
            logger.error("AI生成失败: {}", response.getErrorMessage());
            return "抱歉，AI服务暂时不可用，请稍后再试。";
        }
    }

    /**
     * 构建系统消息
     */
    private com.alibaba.dashscope.common.Message systemMessage(String content) {
        return com.alibaba.dashscope.common.Message.builder()
                .role("system")
                .content(content)
                .build();
    }

    /**
     * 构建用户消息
     */
    private com.alibaba.dashscope.common.Message userMessage(String content) {
        return com.alibaba.dashscope.common.Message.builder()
                .role("user")
                .content(content)
                .build();
    }
}