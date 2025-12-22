package com.tencent.wxcloudrun.dto.collection;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收藏列表项DTO
 */
@Data
public class CollectionListItem {

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
     * 人数
     */
    private Integer people;

    /**
     * 总预算
     */
    private BigDecimal budget;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}