package com.wy.test.persistence.service;

import java.util.List;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.Resources;
import com.wy.test.persistence.mapper.ResourcesMapper;

@Repository
public class ResourcesService extends JpaService<Resources> {

	public ResourcesService() {
		super(ResourcesMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public ResourcesMapper getMapper() {
		return (ResourcesMapper) super.getMapper();
	}

	public List<Resources> queryResourcesTree(Resources resource) {
		return fetchPageResults(resource).getRows();
	}
}