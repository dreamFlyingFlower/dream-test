package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.HistoryLoginApps;
import com.wy.test.persistence.mapper.HistoryLoginAppsMapper;

@Repository
public class HistoryLoginAppsService extends JpaService<HistoryLoginApps> {

	public HistoryLoginAppsService() {
		super(HistoryLoginAppsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public HistoryLoginAppsMapper getMapper() {
		return (HistoryLoginAppsMapper) super.getMapper();
	}

	@Override
	public boolean insert(HistoryLoginApps loginAppsHistory) {
		return getMapper().insert(loginAppsHistory) > 0;
	}
}
