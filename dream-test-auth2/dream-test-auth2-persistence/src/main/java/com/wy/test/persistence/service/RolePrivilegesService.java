package com.wy.test.persistence.service;

import java.util.List;

import org.dromara.mybatis.jpa.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wy.test.entity.RolePrivileges;
import com.wy.test.persistence.mapper.RolePrivilegesMapper;

@Repository
public class RolePrivilegesService extends JpaService<RolePrivileges> {

	final static Logger _logger = LoggerFactory.getLogger(RolePrivilegesService.class);

	public RolePrivilegesService() {
		super(RolePrivilegesMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public RolePrivilegesMapper getMapper() {
		return (RolePrivilegesMapper) super.getMapper();
	}

	public boolean insertRolePrivileges(List<RolePrivileges> rolePermissionsList) {
		return getMapper().insertRolePrivileges(rolePermissionsList) > 0;
	};

	public boolean deleteRolePrivileges(List<RolePrivileges> rolePermissionsList) {
		return getMapper().deleteRolePrivileges(rolePermissionsList) >= 0;
	}

	public List<RolePrivileges> queryRolePrivileges(RolePrivileges rolePermissions) {
		return getMapper().queryRolePrivileges(rolePermissions);
	}

}
