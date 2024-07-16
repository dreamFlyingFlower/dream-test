package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.LdapContext;
import com.wy.test.persistence.mapper.LdapContextMapper;

@Repository
public class LdapContextService extends JpaService<LdapContext> {

	public LdapContextService() {
		super(LdapContextMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public LdapContextMapper getMapper() {
		return (LdapContextMapper) super.getMapper();
	}

}
