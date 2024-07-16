package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.entity.Organizations;

public interface OrganizationsMapper extends IJpaMapper<Organizations> {

	public List<Organizations> queryOrgs(Organizations organization);
}
