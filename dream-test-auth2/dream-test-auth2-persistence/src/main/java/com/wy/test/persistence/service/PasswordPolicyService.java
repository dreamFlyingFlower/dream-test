package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.PasswordPolicy;
import com.wy.test.persistence.mapper.PasswordPolicyMapper;

@Repository
public class PasswordPolicyService extends JpaService<PasswordPolicy> {

	public PasswordPolicyService() {
		super(PasswordPolicyMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public PasswordPolicyMapper getMapper() {
		return (PasswordPolicyMapper) super.getMapper();
	}

}
