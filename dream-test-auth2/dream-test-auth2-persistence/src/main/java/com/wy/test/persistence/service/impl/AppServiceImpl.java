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

	protected final static Cache<String, AppVO> DETAILS_CACHE =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();

	@Override
	public boolean updateExtendAttr(AppEntity app) {
		return lambdaUpdate().set(AppEntity::getExtendAttr, app.getExtendAttr())
				.eq(AppEntity::getId, app.getId())
				.update();
	}

	@Override
	public List<UserApps> queryMyApps(UserApps userApplications) {
		return baseMapper.queryMyApps(userApplications);
	}

	@Override
	public void put(String appId, AppVO appDetails) {
		DETAILS_CACHE.put(appId + DETAIL_SUFFIX, appDetails);
	}

	@Override
	public AppVO get(String appId, boolean cached) {
		appId = appId.equalsIgnoreCase("dream_mgt") ? MGT_APP_ID : appId;
		AppVO appDetails = null;
		if (cached) {
			appDetails = DETAILS_CACHE.getIfPresent(appId + DETAIL_SUFFIX);
			if (null == appDetails) {
				AppEntity appEntity = this.getById(appId);
				// 没有指定App的访问权限,直接跳转
				if (null != appEntity) {
					appDetails = baseConvert.convertt(appEntity);
					DETAILS_CACHE.put(appId, appDetails);
				}
			}
		} else {
			AppEntity appEntity = this.getById(appId);
			if (null != appEntity) {
				appDetails = baseConvert.convertt(appEntity);
			}
		}
		return appDetails;
	}

}