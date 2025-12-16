package com.tencent.wxcloudrun.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 微信用户实体类
 */
@Data
public class WechatUser implements Serializable {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 微信OpenID
     */
    private String openId;

    /**
     * 微信UnionID
     */
    private String unionId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}