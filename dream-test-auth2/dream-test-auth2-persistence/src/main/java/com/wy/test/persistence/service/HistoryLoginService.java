package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.query.HistoryLoginQuery;
import com.wy.test.core.vo.HistoryLoginVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 登录历史
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface HistoryLoginService extends BaseServices<HistoryLoginEntity, HistoryLoginVO, HistoryLoginQuery> {

	List<HistoryLoginEntity> queryOnlineSession(HistoryLoginEntity historyLogin);
}