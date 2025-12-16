package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.WechatLoginRequest;
import com.tencent.wxcloudrun.dto.WechatLoginResponse;

/**
 * 微信用户服务接口
 */
public interface WechatUserService {

    /**
     * 微信小程序登录
     * @param request 登录请求
     * @return 登录响应
     * @throws Exception 登录失败时抛出异常
     */
    WechatLoginResponse login(WechatLoginRequest request) throws Exception;
}