package com.tencent.wxcloudrun.dto.collection;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收藏详情DTO
 */
@Data
public class CollectionDetail {

    /**
     * 收藏ID
     */
    private Long id;

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
     * 攻略内容(完整攻略)
     */
    private String planContent;

    /**
     * Token使用数
     */
    private Integer totalTokens;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}