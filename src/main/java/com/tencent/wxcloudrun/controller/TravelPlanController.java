package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.RequireToken;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResultCode;
import com.tencent.wxcloudrun.context.UserContextHolder;
import com.tencent.wxcloudrun.dto.travel.TravelPlanResponse;
import com.tencent.wxcloudrun.dto.travel.TravelRequest;
import com.tencent.wxcloudrun.service.TravelPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 旅游攻略生成控制器
 */
@RestController
@RequestMapping("/api/travel")
public class TravelPlanController {

    private static final Logger logger = LoggerFactory.getLogger(TravelPlanController.class);

    @Autowired
    private TravelPlanService travelPlanService;

    /**
     * 生成旅游攻略接口(修改为支持保存历史记录)
     * POST /api/travel/plan/generate
     */
    @RequireToken
    @PostMapping("/plan/generate")
    public ApiResponse generatePlan(@RequestBody TravelRequest request) {
        logger.info("收到旅游攻略生成请求: {} -> {}, {}天, {}人, ¥{}",
                request.getFromCity(), request.getToCity(),
                request.getDays(), request.getPeople(), request.getBudget());

        // 参数校验
        String validationError = validateRequest(request);
        if (validationError != null) {
            logger.warn("参数校验失败: {}", validationError);
            return ApiResponse.error(ResultCode.TRAVEL_PARAM_INVALID);
        }

        // 获取用户ID
        Integer userId = UserContextHolder.getUserId();

        // 调用服务生成攻略并保存历史
        TravelPlanResponse response = travelPlanService.generateAndSaveHistory(request, userId);

        if (response.getSuccess()) {
            logger.info("旅游攻略生成成功, requestId: {}, canCollect: {}", response.getRequestId(), response.getCanCollect());
            return ApiResponse.ok(response);
        } else {
            logger.error("旅游攻略生成失败: {}", response.getErrorMessage());
            return ApiResponse.error(ResultCode.TRAVEL_PLAN_GENERATE_FAIL);
        }
    }

    /**
     * 验证请求参数
     */
    private String validateRequest(TravelRequest request) {
        if (request.getFromCity() == null || request.getFromCity().trim().isEmpty()) {
            return "出发城市不能为空";
        }
        if (request.getToCity() == null || request.getToCity().trim().isEmpty()) {
            return "目的地城市不能为空";
        }
        if (request.getDays() == null || request.getDays() < 1 || request.getDays() > 7) {
            return "游玩天数必须在1-7天之间";
        }
        if (request.getPeople() == null || request.getPeople() < 1 || request.getPeople() > 10) {
            return "人数必须在1-10人之间";
        }
        if (request.getTransport() == null || request.getTransport().trim().isEmpty()) {
            return "出行方式不能为空";
        }
        // 验证出行方式是否在允许的范围内
        String transport = request.getTransport();
        if (!transport.equals("高铁") && !transport.equals("飞机") &&
                !transport.equals("自驾") && !transport.equals("高铁+租车")) {
            return "出行方式只能是：高铁、飞机、自驾、高铁+租车";
        }
        if (request.getBudget() == null || request.getBudget() <= 0) {
            return "总预算必须大于0";
        }
        if (request.getBudget() > 100000) {
            return "总预算不能超过10万元";
        }

        return null; // 验证通过
    }
}