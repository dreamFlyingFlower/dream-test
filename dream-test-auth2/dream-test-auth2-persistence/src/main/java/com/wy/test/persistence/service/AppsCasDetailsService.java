package com.wy.test.persistence.service;

import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.entity.apps.AppsCasDetails;
import com.wy.test.persistence.mapper.AppsCasDetailsMapper;

@Repository
public class AppsCasDetailsService extends JpaService<AppsCasDetails> {

	protected final static Cache<String, AppsCasDetails> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	public AppsCasDetailsService() {
		super(AppsCasDetailsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsCasDetailsMapper getMapper() {
		return (AppsCasDetailsMapper) super.getMapper();
	}

	public AppsCasDetails getAppDetails(String id, boolean cached) {
		AppsCasDetails details = null;
		if (cached) {
			details = detailsCache.getIfPresent(id);
			if (details == null) {
				details = getMapper().getAppDetails(id);
				if (details != null) {
					detailsCache.put(id, details);
				}
			}
		} else {
			details = getMapper().getAppDetails(id);
		}
		return details;
	}
}
