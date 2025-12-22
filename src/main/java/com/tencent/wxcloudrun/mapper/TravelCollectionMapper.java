package com.tencent.wxcloudrun.mapper;

import com.tencent.wxcloudrun.model.TravelCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 旅游攻略收藏表Mapper
 */
@Mapper
public interface TravelCollectionMapper {

    /**
     * 插入收藏
     */
    int insert(TravelCollection collection);

    /**
     * 根据ID查询
     */
    TravelCollection selectById(@Param("id") Long id);

    /**
     * 分页查询用户收藏列表
     */
    List<TravelCollection> selectByUserId(
            @Param("userId") Integer userId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 统计用户收藏总数
     */
    int countByUserId(@Param("userId") Integer userId);

    /**
     * 更新标题
     */
    int updateTitle(@Param("id") Long id, @Param("title") String title);

    /**
     * 软删除
     */
    int updateDeleteStatus(@Param("id") Long id, @Param("isDeleted") Boolean isDeleted);
}