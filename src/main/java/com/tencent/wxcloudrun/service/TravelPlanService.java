package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.travel.TravelPlanResponse;
import com.tencent.wxcloudrun.dto.travel.TravelRequest;

/**
 * 旅游攻略生成服务接口
 */
public interface TravelPlanService {

    /**
     * 生成旅游攻略
     * @param request 旅游请求参数
     * @return 旅游攻略响应
     */
    TravelPlanResponse generateTravelPlan(TravelRequest request);

    /**
     * 生成旅游攻略并保存到历史表
     * @param request 旅游请求参数
     * @param userId 用户ID
     * @return 旅游攻略响应
     */
    TravelPlanResponse generateAndSaveHistory(TravelRequest request, Integer userId);
}