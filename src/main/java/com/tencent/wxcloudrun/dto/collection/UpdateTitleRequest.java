package com.tencent.wxcloudrun.dto.collection;

import lombok.Data;

/**
 * 更新收藏标题请求参数
 */
@Data
public class UpdateTitleRequest {

    /**
     * 收藏ID
     */
    private Long id;

    /**
     * 新标题
     */
    private String title;
}