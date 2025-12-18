package com.tencent.wxcloudrun.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.config.WechatConfig;
import com.tencent.wxcloudrun.dao.WechatUserMapper;
import com.tencent.wxcloudrun.dto.WechatLoginRequest;
import com.tencent.wxcloudrun.dto.WechatLoginResponse;
import com.tencent.wxcloudrun.model.WechatUser;
import com.tencent.wxcloudrun.service.WechatUserService;
import com.tencent.wxcloudrun.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 微信用户服务实现类
 */
@Service
public class WechatUserServiceImpl implements WechatUserService {

    private static final Logger logger = LoggerFactory.getLogger(WechatUserServiceImpl.class);

    /**
     * 微信code2session接口地址
     */
    private static final String CODE2SESSION_URL =
        "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private WechatUserMapper wechatUserMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public WechatLoginResponse login(WechatLoginRequest request) throws Exception {
        // 1. 调用微信code2session接口，获取openid和unionid
        String code2SessionUrl = String.format(
            CODE2SESSION_URL,
            wechatConfig.getAppId(),
            wechatConfig.getAppSecret(),
            request.getCode()
        );

        logger.info("调用微信code2session接口");
        String sessionResult = restTemplate.getForObject(code2SessionUrl, String.class);
        logger.info("微信code2session返回结果: {}", sessionResult);

        // 2. 解析微信返回结果
        JsonNode sessionNode = objectMapper.readTree(sessionResult);

        // 检查是否有错误
        if (sessionNode.has("errcode") && sessionNode.get("errcode").asInt() != 0) {
            String errorMsg = sessionNode.get("errmsg").asText();
            logger.error("获取微信session失败: {}", errorMsg);
            throw new RuntimeException("获取微信session失败: " + errorMsg);
        }

        String openId = sessionNode.get("openid").asText();

        logger.info("获取到openId: {}", openId);

        // 3. 查询数据库，检查用户是否存在
        WechatUser existingUser = wechatUserMapper.selectByOpenId(openId);
        boolean isNewUser = (existingUser == null);

        // 4. 保存或更新用户信息
        WechatUser user = new WechatUser();
        user.setOpenId(openId);

        if (isNewUser) {
            // 新用户：插入数据库
            wechatUserMapper.insert(user);
            logger.info("新用户注册成功, userId: {}", user.getId());
        } else {
            // 老用户：使用已存在的userId
            user.setId(existingUser.getId());
        }

        // 5. 生成token
        String token;
        try {
            token = jwtUtil.generateToken(user.getId());
            logger.info("生成token成功, userId: {}", user.getId());
        } catch (Exception e) {
            logger.error("生成token失败, userId: {}", user.getId(), e);
            throw new RuntimeException("token生成失败");
        }

        // 6. 构建响应（不返回敏感信息）
        WechatLoginResponse response = new WechatLoginResponse();
        response.setUserId(user.getId());
        response.setIsNewUser(isNewUser);
        response.setToken(token);

        return response;
    }
}