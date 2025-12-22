package com.tencent.wxcloudrun.mapper;

import com.tencent.wxcloudrun.model.TravelPlanHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 旅游攻略生成历史表Mapper
 */
@Mapper
public interface TravelPlanHistoryMapper {

    /**
     * 插入历史记录
     */
    int insert(TravelPlanHistory history);

    /**
     * 根据requestId查询(用于收藏时查询)
     */
    TravelPlanHistory selectByRequestId(@Param("requestId") String requestId);

    /**
     * 删除过期记录(定时任务调用)
     */
    int deleteExpired();
}