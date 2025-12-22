package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 旅游攻略生成历史表
 */
@Data
public class TravelPlanHistory implements Serializable {

    /**
     * 历史ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * AI请求ID(唯一,前端收藏时传此ID)
     */
    private String requestId;

    /**
     * 出发城市
     */
    private String fromCity;

    /**
     * 目的地城市
     */
    private String toCity;

    /**
     * 游玩天数
     */
    private Integer days;

    /**
     * 出发日期
     */
    private String startDate;

    /**
     * 人数
     */
    private Integer people;

    /**
     * 出行方式
     */
    private String transport;

    /**
     * 总预算
     */
    private BigDecimal budget;

    /**
     * 特殊需求
     */
    private String specialNeeds;

    /**
     * 攻略内容
     */
    private String planContent;

    /**
     * Token使用数
     */
    private Integer totalTokens;

    /**
     * 过期时间(7天后自动清理)
     */
    private LocalDateTime expireAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}