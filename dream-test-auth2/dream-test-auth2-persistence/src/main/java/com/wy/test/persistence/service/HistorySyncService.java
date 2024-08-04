package com.wy.test.persistence.service;

import com.wy.test.core.entity.HistorySyncEntity;
import com.wy.test.core.query.HistorySyncQuery;
import com.wy.test.core.vo.HistorySyncVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 同步日志
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface HistorySyncService extends BaseServices<HistorySyncEntity, HistorySyncVO, HistorySyncQuery> {

}