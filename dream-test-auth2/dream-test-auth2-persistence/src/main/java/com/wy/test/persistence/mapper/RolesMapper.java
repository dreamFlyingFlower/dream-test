package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.entity.Roles;


public interface RolesMapper extends IJpaMapper<Roles> {

	public List<Roles> queryDynamicRoles(Roles groups);

	public List<Roles> queryRolesByUserId(String userId);
}
