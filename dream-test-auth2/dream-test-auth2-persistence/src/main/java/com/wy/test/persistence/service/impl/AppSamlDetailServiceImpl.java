package com.wy.test.persistence.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.convert.AppSamlDetailConvert;
import com.wy.test.core.entity.AppSamlDetailEntity;
import com.wy.test.core.query.AppSamlDetailQuery;
import com.wy.test.core.vo.AppSamlDetailVO;
import com.wy.test.persistence.mapper.AppSamlDetailMapper;
import com.wy.test.persistence.service.AppSamlDetailService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * SAML详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppSamlDetailServiceImpl extends AbstractServiceImpl<AppSamlDetailEntity, AppSamlDetailVO,
		AppSamlDetailQuery, AppSamlDetailConvert, AppSamlDetailMapper> implements AppSamlDetailService {

	protected final static Cache<String, AppSamlDetailVO> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	@Override
	public AppSamlDetailVO getAppDetails(String id, boolean cached) {
		AppSamlDetailVO details = null;
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