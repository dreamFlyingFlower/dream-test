package com.wy.test.persistence.service;

import java.util.List;

import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.query.RoleQuery;
import com.wy.test.core.vo.RoleVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 角色
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RoleService extends BaseServices<RoleEntity, RoleVO, RoleQuery> {

	List<RoleEntity> queryDynamicRoles(RoleEntity groups);

	boolean deleteById(String groupId);

	List<RoleEntity> queryRolesByUserId(String userId);

	void refreshDynamicRoles(RoleEntity dynamicRole);

	void refreshAllDynamicRoles();
}