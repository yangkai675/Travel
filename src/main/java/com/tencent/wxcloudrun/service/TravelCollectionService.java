package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.collection.CollectionDetail;
import com.tencent.wxcloudrun.dto.collection.CollectionListResponse;

/**
 * 旅游攻略收藏服务
 */
public interface TravelCollectionService {

    /**
     * 根据requestId收藏攻略
     */
    Long saveCollectionByRequestId(String requestId, String title, Integer userId);

    /**
     * 获取收藏列表(分页)
     */
    CollectionListResponse getCollectionList(Integer userId, Integer pageNum, Integer pageSize);

    /**
     * 获取收藏详情
     */
    CollectionDetail getCollectionDetail(Long id, Integer userId);

    /**
     * 更新收藏标题
     */
    void updateTitle(Long id, String title, Integer userId);

    /**
     * 删除收藏(软删除)
     */
    void deleteCollection(Long id, Integer userId);
}