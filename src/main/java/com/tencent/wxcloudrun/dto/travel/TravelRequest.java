package com.tencent.wxcloudrun.dto.travel;

import lombok.Data;

/**
 * 旅游攻略生成请求参数
 */
@Data
public class TravelRequest {

    /**
     * 出发城市（如 "太原市"）
     */
    private String fromCity;

    /**
     * 目的地城市（如 "博尔塔拉蒙古自治州"）
     */
    private String toCity;

    /**
     * 游玩天数（1~7）
     */
    private Integer days;

    /**
     * 出发日期（YYYY-MM-DD）
     */
    private String startDate;

    /**
     * 人数（1~10）
     */
    private Integer people;

    /**
     * 出行方式：高铁 / 飞机 / 自驾 / 高铁+租车
     */
    private String transport;

    /**
     * 总预算（元）
     */
    private Double budget;

    /**
     * 特殊需求（可选，如 "带老人"、"想看日出"）
     */
    private String specialNeeds;
}