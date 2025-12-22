package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.annotation.RequireToken;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResultCode;
import com.tencent.wxcloudrun.context.UserContextHolder;
import com.tencent.wxcloudrun.dto.collection.CollectionDetail;
import com.tencent.wxcloudrun.dto.collection.CollectionListResponse;
import com.tencent.wxcloudrun.dto.collection.SaveCollectionRequest;
import com.tencent.wxcloudrun.dto.collection.UpdateTitleRequest;
import com.tencent.wxcloudrun.exception.BusinessException;
import com.tencent.wxcloudrun.service.TravelCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 旅游攻略收藏控制器
 */
@RestController
@RequestMapping("/api/travel")
public class TravelCollectionController {

    private static final Logger logger = LoggerFactory.getLogger(TravelCollectionController.class);

    @Autowired
    private TravelCollectionService collectionService;

    /**
     * 收藏攻略(根据requestId)
     * POST /api/travel/collection/save
     */
    @RequireToken
    @PostMapping("/collection/save")
    public ApiResponse saveCollection(@RequestBody SaveCollectionRequest request) {
        logger.info("收到收藏攻略请求, requestId: {}", request.getRequestId());

        // 参数校验
        if (request.getRequestId() == null || request.getRequestId().trim().isEmpty()) {
            logger.warn("requestId不能为空");
            return ApiResponse.error(ResultCode.PARAM_ERROR);
        }

        try {
            // 获取用户ID
            Integer userId = UserContextHolder.getUserId();

            // 根据requestId收藏
            Long collectionId = collectionService.saveCollectionByRequestId(
                    request.getRequestId(),
                    request.getTitle(),
                    userId
            );

            Map<String, Object> result = new HashMap<>();
            result.put("collectionId", collectionId);
            result.put("message", "收藏成功");

            logger.info("收藏成功, collectionId: {}, userId: {}", collectionId, userId);
            return ApiResponse.ok(result);

        } catch (BusinessException e) {
            logger.warn("收藏失败: {}", e.getMessage());
            return ApiResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("收藏异常", e);
            return ApiResponse.error(ResultCode.SYSTEM_ERROR);
        }
    }

    /**
     * 获取收藏列表(分页)
     * GET /api/travel/collection/list
     */
    @RequireToken
    @GetMapping("/collection/list")
    public ApiResponse getCollectionList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        logger.info("获取收藏列表, pageNum: {}, pageSize: {}", pageNum, pageSize);

        // 参数校验
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }

        try {
            // 获取用户ID
            Integer userId = UserContextHolder.getUserId();

            // 查询收藏列表
            CollectionListResponse response = collectionService.getCollectionList(userId, pageNum, pageSize);

            logger.info("获取收藏列表成功, 用户: {}, 总数: {}", userId, response.getTotal());
            return ApiResponse.ok(response);

        } catch (Exception e) {
            logger.error("获取收藏列表异常", e);
            return ApiResponse.error(ResultCode.SYSTEM_ERROR);
        }
    }

    /**
     * 获取收藏详情
     * GET /api/travel/collection/{id}
     */
    @RequireToken
    @GetMapping("/collection/{id}")
    public ApiResponse getCollectionDetail(@PathVariable Long id) {
        logger.info("获取收藏详情, id: {}", id);

        try {
            // 获取用户ID
            Integer userId = UserContextHolder.getUserId();

            // 查询收藏详情
            CollectionDetail detail = collectionService.getCollectionDetail(id, userId);

            logger.info("获取收藏详情成功, id: {}", id);
            return ApiResponse.ok(detail);

        } catch (BusinessException e) {
            logger.warn("获取收藏详情失败: {}", e.getMessage());
            return ApiResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("获取收藏详情异常", e);
            return ApiResponse.error(ResultCode.SYSTEM_ERROR);
        }
    }

    /**
     * 修改收藏标题
     * POST /api/travel/collection/title
     */
    @RequireToken
    @PostMapping("/collection/title")
    public ApiResponse updateTitle(@RequestBody UpdateTitleRequest request) {
        logger.info("修改收藏标题, id: {}, newTitle: {}", request.getId(), request.getTitle());

        try {
            // 获取用户ID
            Integer userId = UserContextHolder.getUserId();

            // 更新标题
            collectionService.updateTitle(request.getId(), request.getTitle(), userId);

            Map<String, Object> result = new HashMap<>();
            result.put("message", "修改成功");

            logger.info("修改收藏标题成功, id: {}", request.getId());
            return ApiResponse.ok(result);

        } catch (BusinessException e) {
            logger.warn("修改收藏标题失败: {}", e.getMessage());
            return ApiResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("修改收藏标题异常", e);
            return ApiResponse.error(ResultCode.SYSTEM_ERROR);
        }
    }

    /**
     * 删除收藏(软删除)
     * GET /api/travel/collection/{id}
     */
    @RequireToken
    @GetMapping("/collection/{id}")
    public ApiResponse deleteCollection(@PathVariable Long id) {
        logger.info("删除收藏, id: {}", id);

        try {
            // 获取用户ID
            Integer userId = UserContextHolder.getUserId();

            // 删除收藏
            collectionService.deleteCollection(id, userId);

            Map<String, Object> result = new HashMap<>();
            result.put("message", "删除成功");

            logger.info("删除收藏成功, id: {}", id);
            return ApiResponse.ok(result);

        } catch (BusinessException e) {
            logger.warn("删除收藏失败: {}", e.getMessage());
            return ApiResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("删除收藏异常", e);
            return ApiResponse.error(ResultCode.SYSTEM_ERROR);
        }
    }
}