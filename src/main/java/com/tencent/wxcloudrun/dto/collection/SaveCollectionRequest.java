package com.tencent.wxcloudrun.dto.collection;

import lombok.Data;

/**
 * 收藏攻略请求参数(简化版)
 */
@Data
public class SaveCollectionRequest {

    /**
     * AI请求ID(从历史表查询)
     */
    private String requestId;

    /**
     * 收藏标题(可选,不传则自动生成)
     */
    private String title;
}