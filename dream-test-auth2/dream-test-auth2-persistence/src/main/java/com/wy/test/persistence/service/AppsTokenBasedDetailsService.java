package com.wy.test.persistence.service;

import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.apps.AppsTokenBasedDetails;
import com.wy.test.persistence.mapper.AppsTokenBasedDetailsMapper;

@Repository
public class AppsTokenBasedDetailsService extends JpaService<AppsTokenBasedDetails> {

	protected final static Cache<String, AppsTokenBasedDetails> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	public AppsTokenBasedDetailsService() {
		super(AppsTokenBasedDetailsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsTokenBasedDetailsMapper getMapper() {
		return (AppsTokenBasedDetailsMapper) super.getMapper();
	}

	public AppsTokenBasedDetails getAppDetails(String id, boolean cached) {
		AppsTokenBasedDetails details = null;
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
