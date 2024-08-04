package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.RolePrivilegeEntity;
import com.wy.test.core.query.RolePrivilegeQuery;
import com.wy.test.core.vo.RolePrivilegeVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RolePrivilegeService extends BaseServices<RolePrivilegeEntity, RolePrivilegeVO, RolePrivilegeQuery> {

	boolean insertRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList);

	boolean deleteRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList);

	List<RolePrivilegeEntity> queryRolePrivileges(RolePrivilegeEntity rolePermissions);
}