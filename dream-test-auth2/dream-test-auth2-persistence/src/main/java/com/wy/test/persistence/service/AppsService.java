package com.wy.test.persistence.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.entity.apps.UserApps;
import com.wy.test.persistence.mapper.AppsMapper;

@Repository
public class AppsService extends JpaService<Apps> {

	// maxkey-mgt
	public final static String MGT_APP_ID = "622076759805923328";

	public final static String DETAIL_SUFFIX = "_detail";

	protected final static Cache<String, Apps> detailsCacheStore =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();

	public AppsService() {
		super(AppsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsMapper getMapper() {
		return (AppsMapper) super.getMapper();
	}

	public boolean insertApp(Apps app) {
		return ((AppsMapper) super.getMapper()).insertApp(app) > 0;
	};

	public boolean updateApp(Apps app) {
		return ((AppsMapper) super.getMapper()).updateApp(app) > 0;
	};

	public boolean updateExtendAttr(Apps app) {
		return ((AppsMapper) super.getMapper()).updateExtendAttr(app) > 0;
	}

	public List<UserApps> queryMyApps(UserApps userApplications) {
		return getMapper().queryMyApps(userApplications);
	}

	// cache for running
	public void put(String appId, Apps appDetails) {
		detailsCacheStore.put(appId + DETAIL_SUFFIX, appDetails);
	}

	public Apps get(String appId, boolean cached) {
		appId = appId.equalsIgnoreCase("maxkey_mgt") ? MGT_APP_ID : appId;
		Apps appDetails = null;
		if (cached) {
			appDetails = detailsCacheStore.getIfPresent(appId + DETAIL_SUFFIX);
			if (appDetails == null) {
				appDetails = this.get(appId);
				detailsCacheStore.put(appId, appDetails);
			}
		} else {
			appDetails = this.get(appId);
		}
		return appDetails;
	}

}
