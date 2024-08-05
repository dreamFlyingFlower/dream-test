package com.wy.test.persistence.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.convert.AppCasDetailConvert;
import com.wy.test.core.entity.AppCasDetailEntity;
import com.wy.test.core.query.AppCasDetailQuery;
import com.wy.test.core.vo.AppCasDetailVO;
import com.wy.test.persistence.mapper.AppCasDetailMapper;
import com.wy.test.persistence.service.AppCasDetailService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import lombok.AllArgsConstructor;

/**
 * CAS详情
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class AppCasDetailServiceImpl extends AbstractServiceImpl<AppCasDetailEntity, AppCasDetailVO, AppCasDetailQuery,
		AppCasDetailConvert, AppCasDetailMapper> implements AppCasDetailService {

	protected final static Cache<String, AppCasDetailVO> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	@Override
	public AppCasDetailVO getAppDetails(String id, boolean cached) {
		AppCasDetailVO details = null;
		if (cached) {
			details = detailsCache.getIfPresent(id);
			if (details == null) {
				details = baseMapper.getAppDetails(id);
				if (details != null) {
					detailsCache.put(id, details);
				}
			}
		} else {
			details = baseMapper.getAppDetails(id);
		}
		return details;
	}
}