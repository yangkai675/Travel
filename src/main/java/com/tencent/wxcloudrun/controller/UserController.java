package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResultCode;
import com.tencent.wxcloudrun.dto.WechatLoginRequest;
import com.tencent.wxcloudrun.dto.WechatLoginResponse;
import com.tencent.wxcloudrun.service.WechatUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WechatUserService wechatUserService;

    /**
     * 微信小程序登录接口
     * @param request 登录请求
     * @return 统一响应格式
     */
    @PostMapping("/login")
    public ApiResponse login(@RequestBody WechatLoginRequest request) {
        logger.info("收到登录请求");

        try {
            // 参数校验
            if (request.getCode() == null || request.getCode().isEmpty()) {
                logger.warn("登录失败: code不能为空");
                return ApiResponse.error(ResultCode.PARAM_ERROR, "code不能为空");
            }

            // 执行登录
            WechatLoginResponse response = wechatUserService.login(request);

            logger.info("登录成功, userId: {}, isNewUser: {}", response.getUserId(), response.getIsNewUser());
            return ApiResponse.ok(response);

        } catch (Exception e) {
            logger.error("登录失败", e);
            // 根据异常类型返回不同的错误码
            if (e.getMessage().contains("invalid code")) {
                return ApiResponse.error(ResultCode.WECHAT_CODE_INVALID);
            }
            return ApiResponse.error(ResultCode.WECHAT_API_ERROR, e.getMessage());
        }
    }
}