package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.config.ResultCode;
import com.tencent.wxcloudrun.dto.collection.CollectionDetail;
import com.tencent.wxcloudrun.dto.collection.CollectionListItem;
import com.tencent.wxcloudrun.dto.collection.CollectionListResponse;
import com.tencent.wxcloudrun.exception.BusinessException;
import com.tencent.wxcloudrun.mapper.TravelCollectionMapper;
import com.tencent.wxcloudrun.mapper.TravelPlanHistoryMapper;
import com.tencent.wxcloudrun.model.TravelCollection;
import com.tencent.wxcloudrun.model.TravelPlanHistory;
import com.tencent.wxcloudrun.service.TravelCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 旅游攻略收藏服务实现
 */
@Service
public class TravelCollectionServiceImpl implements TravelCollectionService {

    private static final Logger logger = LoggerFactory.getLogger(TravelCollectionServiceImpl.class);

    @Autowired
    private TravelCollectionMapper collectionMapper;

    @Autowired
    private TravelPlanHistoryMapper historyMapper;

    @Override
    public Long saveCollectionByRequestId(String requestId, String title, Integer userId) {
        logger.info("用户 {} 尝试收藏攻略, requestId: {}", userId, requestId);

        // 1. 从历史表查询
        TravelPlanHistory history = historyMapper.selectByRequestId(requestId);

        if (history == null) {
            logger.warn("攻略历史记录不存在, requestId: {}", requestId);
            throw new BusinessException(ResultCode.COLLECTION_HISTORY_NOT_FOUND);
        }

        // 2. 验证归属
        if (!history.getUserId().equals(userId)) {
            logger.warn("用户 {} 无权限收藏攻略, requestId: {}, 攻略归属用户: {}", userId, requestId, history.getUserId());
            throw new BusinessException(ResultCode.COLLECTION_NO_PERMISSION);
        }

        // 3. 检查是否过期
        if (history.getExpireAt().isBefore(LocalDateTime.now())) {
            logger.warn("攻略已过期, requestId: {}, 过期时间: {}", requestId, history.getExpireAt());
            throw new BusinessException(ResultCode.COLLECTION_HISTORY_EXPIRED);
        }

        // 4. 创建收藏记录
        TravelCollection collection = new TravelCollection();
        collection.setUserId(userId);

        // 标题: 用户传的 > 自动生成
        if (title == null || title.trim().isEmpty()) {
            title = history.getFromCity().replace("市", "").replace("自治州", "") + "到" +
                    history.getToCity().replace("市", "").replace("自治州", "") +
                    history.getDays() + "日游";
        }
        collection.setTitle(title);

        // 复制所有字段
        collection.setFromCity(history.getFromCity());
        collection.setToCity(history.getToCity());
        collection.setDays(history.getDays());
        collection.setStartDate(history.getStartDate());
        collection.setPeople(history.getPeople());
        collection.setTransport(history.getTransport());
        collection.setBudget(history.getBudget());
        collection.setSpecialNeeds(history.getSpecialNeeds());
        collection.setPlanContent(history.getPlanContent());
        collection.setAiRequestId(history.getRequestId());
        collection.setTotalTokens(history.getTotalTokens());
        collection.setIsDeleted(false);

        // 5. 保存到收藏表
        int rows = collectionMapper.insert(collection);
        if (rows <= 0) {
            logger.error("收藏保存失败, userId: {}, requestId: {}", userId, requestId);
            throw new BusinessException(ResultCode.COLLECTION_SAVE_ERROR);
        }

        logger.info("收藏成功, collectionId: {}, userId: {}", collection.getId(), userId);
        return collection.getId();
    }

    @Override
    public CollectionListResponse getCollectionList(Integer userId, Integer pageNum, Integer pageSize) {
        logger.info("查询用户 {} 的收藏列表, pageNum: {}, pageSize: {}", userId, pageNum, pageSize);

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 查询列表
        List<TravelCollection> collections = collectionMapper.selectByUserId(userId, offset, pageSize);

        // 查询总数
        int total = collectionMapper.countByUserId(userId);

        // 转换为DTO
        List<CollectionListItem> items = collections.stream()
                .map(this::convertToListItem)
                .collect(Collectors.toList());

        // 构建响应
        CollectionListResponse response = new CollectionListResponse();
        response.setTotal((long) total);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setPages((int) Math.ceil((double) total / pageSize));
        response.setList(items);

        logger.info("查询收藏列表成功, 总数: {}, 当前页: {}", total, items.size());
        return response;
    }

    @Override
    public CollectionDetail getCollectionDetail(Long id, Integer userId) {
        logger.info("查询收藏详情, id: {}, userId: {}", id, userId);

        TravelCollection collection = collectionMapper.selectById(id);

        if (collection == null) {
            logger.warn("收藏不存在, id: {}", id);
            throw new BusinessException(ResultCode.COLLECTION_NOT_FOUND);
        }

        // 验证归属
        if (!collection.getUserId().equals(userId)) {
            logger.warn("用户 {} 无权限访问收藏 {}", userId, id);
            throw new BusinessException(ResultCode.COLLECTION_NO_PERMISSION);
        }

        // 转换为DTO
        CollectionDetail detail = new CollectionDetail();
        BeanUtils.copyProperties(collection, detail);

        logger.info("查询收藏详情成功, id: {}", id);
        return detail;
    }

    @Override
    public void updateTitle(Long id, String title, Integer userId) {
        logger.info("更新收藏标题, id: {}, userId: {}, newTitle: {}", id, userId, title);

        // 参数校验
        if (title == null || title.trim().isEmpty()) {
            logger.warn("收藏标题不能为空");
            throw new BusinessException(ResultCode.COLLECTION_TITLE_EMPTY);
        }

        // 查询收藏
        TravelCollection collection = collectionMapper.selectById(id);
        if (collection == null) {
            logger.warn("收藏不存在, id: {}", id);
            throw new BusinessException(ResultCode.COLLECTION_NOT_FOUND);
        }

        // 验证归属
        if (!collection.getUserId().equals(userId)) {
            logger.warn("用户 {} 无权限修改收藏 {}", userId, id);
            throw new BusinessException(ResultCode.COLLECTION_NO_PERMISSION);
        }

        // 更新标题
        int rows = collectionMapper.updateTitle(id, title);
        if (rows <= 0) {
            logger.error("更新收藏标题失败, id: {}", id);
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }

        logger.info("更新收藏标题成功, id: {}", id);
    }

    @Override
    public void deleteCollection(Long id, Integer userId) {
        logger.info("删除收藏, id: {}, userId: {}", id, userId);

        // 查询收藏
        TravelCollection collection = collectionMapper.selectById(id);
        if (collection == null) {
            logger.warn("收藏不存在, id: {}", id);
            throw new BusinessException(ResultCode.COLLECTION_NOT_FOUND);
        }

        // 验证归属
        if (!collection.getUserId().equals(userId)) {
            logger.warn("用户 {} 无权限删除收藏 {}", userId, id);
            throw new BusinessException(ResultCode.COLLECTION_NO_PERMISSION);
        }

        // 软删除
        int rows = collectionMapper.updateDeleteStatus(id, true);
        if (rows <= 0) {
            logger.error("删除收藏失败, id: {}", id);
            throw new BusinessException(ResultCode.SYSTEM_ERROR);
        }

        logger.info("删除收藏成功, id: {}", id);
    }

    /**
     * 转换为列表项DTO
     */
    private CollectionListItem convertToListItem(TravelCollection collection) {
        CollectionListItem item = new CollectionListItem();
        BeanUtils.copyProperties(collection, item);
        return item;
    }
}