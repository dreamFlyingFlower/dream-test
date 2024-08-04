package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.HistoryEventConvert;
import com.wy.test.core.entity.HistoryEventEntity;
import com.wy.test.core.query.HistoryEventQuery;
import com.wy.test.core.vo.HistoryEventVO;
import com.wy.test.persistence.mapper.HistoryEventMapper;
import com.wy.test.persistence.service.HistoryEventService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 历史事件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class HistoryEventServiceImpl extends
		AbstractServiceImpl<HistoryEventEntity, HistoryEventVO, HistoryEventQuery, HistoryEventConvert, HistoryEventMapper >
		implements HistoryEventService {

}