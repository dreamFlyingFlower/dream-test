package com.wy.test.persistence.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.convert.AppConvert;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.query.AppQuery;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserApps;
import com.wy.test.persistence.mapper.AppMapper;
import com.wy.test.persistence.service.AppService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 应用表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class AppServiceImpl extends AbstractServiceImpl<AppEntity, AppVO, AppQuery, AppConvert, AppMapper>
		implements AppService {

	public final static String MGT_APP_ID = "622076759805923328";

	public final static String DETAIL_SUFFIX = "_detail";

	protected final static Cache<String, AppVO> detailsCacheStore =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();

	@Override
	public boolean insertApp(AppVO app) {
		return baseMapper.insertApp(app) > 0;
	}

	@Override
	public boolean updateApp(AppVO app) {
		return baseMapper.updateApp(app) > 0;
	}

	@Override
	public boolean updateExtendAttr(AppEntity app) {
		return baseMapper.updateExtendAttr(app) > 0;
	}

	@Override
	public List<UserApps> queryMyApps(UserApps userApplications) {
		return baseMapper.queryMyApps(userApplications);
	}

	@Override
	public void put(String appId, AppVO appDetails) {
		detailsCacheStore.put(appId + DETAIL_SUFFIX, appDetails);
	}

	@Override
	public AppVO get(String appId, boolean cached) {
		appId = appId.equalsIgnoreCase("dream_mgt") ? MGT_APP_ID : appId;
		AppVO appDetails = null;
		if (cached) {
			appDetails = detailsCacheStore.getIfPresent(appId + DETAIL_SUFFIX);
			if (appDetails == null) {
				AppEntity appEntity = this.getById(appId);
				appDetails = baseConvert.convertt(appEntity);
				detailsCacheStore.put(appId, appDetails);
			}
		} else {
			AppEntity appEntity = this.getById(appId);
			appDetails = baseConvert.convertt(appEntity);
		}
		return appDetails;
	}
}