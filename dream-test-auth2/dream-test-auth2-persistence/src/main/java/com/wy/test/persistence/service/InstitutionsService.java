package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.Institutions;
import com.wy.test.persistence.mapper.InstitutionsMapper;

@Repository
public class InstitutionsService extends JpaService<Institutions> {

	public InstitutionsService() {
		super(InstitutionsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public InstitutionsMapper getMapper() {
		return (InstitutionsMapper) super.getMapper();
	}

	public Institutions findByDomain(String domain) {
		return getMapper().findByDomain(domain);
	};

}
