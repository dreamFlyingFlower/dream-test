package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.RolePrivileges;

public interface RolePrivilegesMapper extends IJpaMapper<RolePrivileges> {

	public int insertRolePrivileges(List<RolePrivileges> rolePermissionsList);

	public int deleteRolePrivileges(List<RolePrivileges> rolePermissionsList);

	public List<RolePrivileges> queryRolePrivileges(RolePrivileges rolePermissions);

}
