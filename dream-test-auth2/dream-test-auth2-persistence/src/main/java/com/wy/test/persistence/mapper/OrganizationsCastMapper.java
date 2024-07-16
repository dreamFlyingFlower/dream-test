package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.entity.Organizations;
import com.wy.test.entity.OrganizationsCast;

public interface OrganizationsCastMapper extends IJpaMapper<OrganizationsCast> {

	public List<Organizations> queryOrgs(Organizations organization);

	public long updateCast(OrganizationsCast organizationsCast);
}
