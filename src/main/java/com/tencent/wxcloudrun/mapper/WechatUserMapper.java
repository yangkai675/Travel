package com.tencent.wxcloudrun.mapper;

import com.tencent.wxcloudrun.model.WechatUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 微信用户Mapper接口
 */
@Mapper
public interface WechatUserMapper {

    /**
     * 根据OpenID查询用户
     * @param openId 微信OpenID
     * @return 用户信息，不存在返回null
     */
    WechatUser selectByOpenId(@Param("openId") String openId);

    /**
     * 插入新用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(WechatUser user);
}