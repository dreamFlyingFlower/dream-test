package com.wy.test.persistence.service;

import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.entity.apps.AppsJwtDetails;
import com.wy.test.persistence.mapper.AppsJwtDetailsMapper;

@Repository
public class AppsJwtDetailsService extends JpaService<AppsJwtDetails> {

	protected final static Cache<String, AppsJwtDetails> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	public AppsJwtDetailsService() {
		super(AppsJwtDetailsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsJwtDetailsMapper getMapper() {
		return (AppsJwtDetailsMapper) super.getMapper();
	}

	public AppsJwtDetails getAppDetails(String id, boolean cached) {
		AppsJwtDetails details = null;
		if (cached) {
			details = detailsCache.getIfPresent(id);
			if (details == null) {
				details = getMapper().getAppDetails(id);
				detailsCache.put(id, details);
			}
		} else {
			details = getMapper().getAppDetails(id);
		}
		return details;
	}
}
