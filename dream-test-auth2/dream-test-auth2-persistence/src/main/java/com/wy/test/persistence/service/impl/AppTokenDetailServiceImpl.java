package com.wy.test.persistence.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.convert.AppTokenDetailConvert;
import com.wy.test.core.entity.AppTokenDetailEntity;
import com.wy.test.core.query.AppTokenDetailQuery;
import com.wy.test.core.vo.AppTokenDetailVO;
import com.wy.test.persistence.mapper.AppTokenDetailMapper;
import com.wy.test.persistence.service.AppTokenDetailService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * token详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppTokenDetailServiceImpl extends AbstractServiceImpl<AppTokenDetailEntity, AppTokenDetailVO,
		AppTokenDetailQuery, AppTokenDetailConvert, AppTokenDetailMapper> implements AppTokenDetailService {

	protected final static Cache<String, AppTokenDetailEntity> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	@Override
	public AppTokenDetailEntity getAppDetails(String id, boolean cached) {
		AppTokenDetailEntity details = null;
		if (cached) {
			details = detailsCache.getIfPresent(id);
			if (details == null) {
				details = baseMapper.getAppDetails(id);
				detailsCache.put(id, details);
			}
		} else {
			details = baseMapper.getAppDetails(id);
		}
		return details;
	}
}