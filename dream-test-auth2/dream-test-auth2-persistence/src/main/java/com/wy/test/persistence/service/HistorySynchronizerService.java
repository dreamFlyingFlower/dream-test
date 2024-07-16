package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.HistorySynchronizer;
import com.wy.test.persistence.mapper.HistorySynchronizerMapper;

@Repository
public class HistorySynchronizerService extends JpaService<HistorySynchronizer> {

	public HistorySynchronizerService() {
		super(HistorySynchronizerMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public HistorySynchronizerMapper getMapper() {
		return (HistorySynchronizerMapper) super.getMapper();
	}
}
