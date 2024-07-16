package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.SocialsProvider;
import com.wy.test.persistence.mapper.SocialsProviderMapper;

@Repository
public class SocialsProviderService extends JpaService<SocialsProvider> {

	final static Logger _logger = LoggerFactory.getLogger(SocialsProviderService.class);

	public SocialsProviderService() {
		super(SocialsProviderMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public SocialsProviderMapper getMapper() {
		return (SocialsProviderMapper) super.getMapper();
	}

}
