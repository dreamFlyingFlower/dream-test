package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.OrganizationsCast;
import com.wy.test.persistence.mapper.OrganizationsCastMapper;

@Repository
public class OrganizationsCastService extends JpaService<OrganizationsCast> {

	final static Logger _logger = LoggerFactory.getLogger(OrganizationsCastService.class);

	public OrganizationsCastService() {
		super(OrganizationsCastMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public OrganizationsCastMapper getMapper() {
		return (OrganizationsCastMapper) super.getMapper();
	}

	public boolean updateCast(OrganizationsCast organizationsCast) {
		return getMapper().updateCast(organizationsCast) > 0;
	}

}
