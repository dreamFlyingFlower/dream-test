package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.Synchronizers;
import com.wy.test.persistence.mapper.SynchronizersMapper;

@Repository
public class SynchronizersService extends JpaService<Synchronizers> {

	final static Logger _logger = LoggerFactory.getLogger(SynchronizersService.class);

	public SynchronizersService() {
		super(SynchronizersMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public SynchronizersMapper getMapper() {
		return (SynchronizersMapper) super.getMapper();
	}

}
