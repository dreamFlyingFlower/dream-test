package com.wy.test.persistence.service;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.RolePermissions;
import com.wy.test.persistence.mapper.RolePermissionsMapper;

@Repository
public class RolePermissionssService extends JpaService<RolePermissions> {

	public RolePermissionssService() {
		super(RolePermissionsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public RolePermissionsMapper getMapper() {
		return (RolePermissionsMapper) super.getMapper();
	}

}
