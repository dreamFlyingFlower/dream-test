package com.wy.test.persistence.service;

import com.wy.test.core.entity.HistoryEventEntity;
import com.wy.test.core.query.HistoryEventQuery;
import com.wy.test.core.vo.HistoryEventVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 历史事件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface HistoryEventService extends BaseServices<HistoryEventEntity, HistoryEventVO, HistoryEventQuery> {

}