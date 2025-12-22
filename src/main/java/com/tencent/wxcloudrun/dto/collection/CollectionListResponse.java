package com.tencent.wxcloudrun.dto.collection;

import lombok.Data;

import java.util.List;

/**
 * 收藏列表分页响应
 */
@Data
public class CollectionListResponse {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 列表数据
     */
    private List<CollectionListItem> list;
}