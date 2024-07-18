package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.HistoryConnector;
import com.wy.test.persistence.mapper.HistoryConnectorMapper;

@Repository
public class HistoryConnectorService extends JpaService<HistoryConnector> {

	public HistoryConnectorService() {
		super(HistoryConnectorMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public HistoryConnectorMapper getMapper() {
		return (HistoryConnectorMapper) super.getMapper();
	}
}
