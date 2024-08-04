package com.wy.test.persistence.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.convert.AppFormDetailConvert;
import com.wy.test.core.entity.AppFormDetailEntity;
import com.wy.test.core.query.AppFormDetailQuery;
import com.wy.test.core.vo.AppFormDetailVO;
import com.wy.test.persistence.mapper.AppFormDetailMapper;
import com.wy.test.persistence.service.AppFormDetailService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 表单信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppFormDetailServiceImpl extends AbstractServiceImpl<AppFormDetailEntity, AppFormDetailVO,
		AppFormDetailQuery, AppFormDetailConvert, AppFormDetailMapper> implements AppFormDetailService {

	protected final static Cache<String, AppFormDetailEntity> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	@Override
	public AppFormDetailEntity getAppDetails(String id, boolean cached) {
		AppFormDetailEntity details = null;
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