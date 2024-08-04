package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.HistoryConnectorConvert;
import com.wy.test.core.entity.HistoryConnectorEntity;
import com.wy.test.core.query.HistoryConnectorQuery;
import com.wy.test.core.vo.HistoryConnectorVO;
import com.wy.test.persistence.mapper.HistoryConnectorMapper;
import com.wy.test.persistence.service.HistoryConnectorService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 历史连接器
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class HistoryConnectorServiceImpl extends AbstractServiceImpl<HistoryConnectorEntity, HistoryConnectorVO,
		HistoryConnectorQuery, HistoryConnectorConvert, HistoryConnectorMapper> implements HistoryConnectorService {

}