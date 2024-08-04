package com.wy.test.persistence.service;

import com.wy.test.core.entity.HistoryLoginAppEntity;
import com.wy.test.core.query.HistoryLoginAppQuery;
import com.wy.test.core.vo.HistoryLoginAppVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * APP登录历史记录
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface HistoryLoginAppService
		extends BaseServices<HistoryLoginAppEntity, HistoryLoginAppVO, HistoryLoginAppQuery> {

	boolean insert(HistoryLoginAppEntity loginAppsHistory);
}