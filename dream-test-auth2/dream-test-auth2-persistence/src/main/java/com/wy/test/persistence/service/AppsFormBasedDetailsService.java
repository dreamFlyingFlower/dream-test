package com.wy.test.persistence.service;

import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.apps.AppsFormBasedDetails;
import com.wy.test.persistence.mapper.AppsFormBasedDetailsMapper;

@Repository
public class AppsFormBasedDetailsService extends JpaService<AppsFormBasedDetails> {

	protected final static Cache<String, AppsFormBasedDetails> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	public AppsFormBasedDetailsService() {
		super(AppsFormBasedDetailsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsFormBasedDetailsMapper getMapper() {
		return (AppsFormBasedDetailsMapper) super.getMapper();
	}

	public AppsFormBasedDetails getAppDetails(String id, boolean cached) {
		AppsFormBasedDetails details = null;
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
