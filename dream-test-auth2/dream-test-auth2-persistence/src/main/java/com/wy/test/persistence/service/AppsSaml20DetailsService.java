package com.wy.test.persistence.service;

import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.apps.AppsSAML20Details;
import com.wy.test.persistence.mapper.AppsSaml20DetailsMapper;

@Repository
public class AppsSaml20DetailsService extends JpaService<AppsSAML20Details> {

	protected final static Cache<String, AppsSAML20Details> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	public AppsSaml20DetailsService() {
		super(AppsSaml20DetailsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsSaml20DetailsMapper getMapper() {
		return (AppsSaml20DetailsMapper) super.getMapper();
	}

	public AppsSAML20Details getAppDetails(String id, boolean cached) {
		AppsSAML20Details details = null;
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
