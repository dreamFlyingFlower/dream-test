package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.Connectors;
import com.wy.test.persistence.mapper.ConnectorsMapper;

@Repository
public class ConnectorsService extends JpaService<Connectors> {

	final static Logger _logger = LoggerFactory.getLogger(ConnectorsService.class);

	public ConnectorsService() {
		super(ConnectorsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public ConnectorsMapper getMapper() {
		return (ConnectorsMapper) super.getMapper();
	}
}
