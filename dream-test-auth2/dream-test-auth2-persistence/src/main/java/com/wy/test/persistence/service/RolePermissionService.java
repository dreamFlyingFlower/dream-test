package com.wy.test.persistence.service;

import com.wy.test.core.entity.RolePermissionEntity;
import com.wy.test.core.query.RolePermissionQuery;
import com.wy.test.core.vo.RolePermissionVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RolePermissionService
		extends BaseServices<RolePermissionEntity, RolePermissionVO, RolePermissionQuery> {

}