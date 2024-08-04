package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.RolePermissionConvert;
import com.wy.test.core.entity.RolePermissionEntity;
import com.wy.test.core.query.RolePermissionQuery;
import com.wy.test.core.vo.RolePermissionVO;
import com.wy.test.persistence.mapper.RolePermissionMapper;
import com.wy.test.persistence.service.RolePermissionService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class RolePermissionServiceImpl extends AbstractServiceImpl<RolePermissionEntity, RolePermissionVO,
		RolePermissionQuery, RolePermissionConvert, RolePermissionMapper> implements RolePermissionService {

}