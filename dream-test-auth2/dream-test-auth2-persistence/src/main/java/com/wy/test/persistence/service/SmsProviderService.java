package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.SmsProvider;
import com.wy.test.persistence.mapper.SmsProviderMapper;

@Repository
public class SmsProviderService extends JpaService<SmsProvider> {

	public SmsProviderService() {
		super(SmsProviderMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public SmsProviderMapper getMapper() {
		return (SmsProviderMapper) super.getMapper();
	}

}
