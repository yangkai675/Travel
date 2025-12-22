package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 旅游攻略收藏表
 */
@Data
public class TravelCollection implements Serializable {

    /**
     * 收藏ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 收藏标题
     */
    private String title;

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
     * AI请求ID
     */
    private String aiRequestId;

    /**
     * Token使用数
     */
    private Integer totalTokens;

    /**
     * 是否删除(0:未删除 1:已删除)
     */
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}