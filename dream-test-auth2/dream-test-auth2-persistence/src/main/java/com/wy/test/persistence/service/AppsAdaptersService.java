package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.apps.AppsAdapters;
import com.wy.test.persistence.mapper.AppsAdaptersMapper;

@Repository
public class AppsAdaptersService extends JpaService<AppsAdapters> {

	public AppsAdaptersService() {
		super(AppsAdaptersMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsAdaptersMapper getMapper() {
		return (AppsAdaptersMapper) super.getMapper();
	}
}
