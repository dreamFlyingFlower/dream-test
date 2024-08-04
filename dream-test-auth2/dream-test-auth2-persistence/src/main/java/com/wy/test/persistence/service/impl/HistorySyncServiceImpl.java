package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.HistorySyncConvert;
import com.wy.test.core.entity.HistorySyncEntity;
import com.wy.test.core.query.HistorySyncQuery;
import com.wy.test.core.vo.HistorySyncVO;
import com.wy.test.persistence.mapper.HistorySyncMapper;
import com.wy.test.persistence.service.HistorySyncService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 同步日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class HistorySyncServiceImpl extends
		AbstractServiceImpl<HistorySyncEntity, HistorySyncVO, HistorySyncQuery, HistorySyncConvert, HistorySyncMapper>
		implements HistorySyncService {

}