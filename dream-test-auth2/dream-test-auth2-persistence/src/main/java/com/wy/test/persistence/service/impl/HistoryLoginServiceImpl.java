package com.wy.test.persistence.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.HistoryLoginConvert;
import com.wy.test.core.entity.HistoryLoginEntity;
import com.wy.test.core.query.HistoryLoginQuery;
import com.wy.test.core.vo.HistoryLoginVO;
import com.wy.test.persistence.mapper.HistoryLoginMapper;
import com.wy.test.persistence.service.HistoryLoginService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 登录历史
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class HistoryLoginServiceImpl extends AbstractServiceImpl<HistoryLoginEntity, HistoryLoginVO, HistoryLoginQuery,
		HistoryLoginConvert, HistoryLoginMapper> implements HistoryLoginService {

	@Override
	public List<HistoryLoginEntity> queryOnlineSession(HistoryLoginEntity historyLogin) {
		return baseMapper.queryOnlineSession(historyLogin);
	}
}