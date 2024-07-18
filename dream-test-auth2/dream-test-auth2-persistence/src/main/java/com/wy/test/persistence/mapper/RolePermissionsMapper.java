package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.core.entity.RolePermissions;

public interface RolePermissionsMapper extends IJpaMapper<RolePermissions> {

	public List<RolePermissions> appsInRole(RolePermissions entity);

	public List<RolePermissions> appsNotInRole(RolePermissions entity);

}
