package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

	@Override
	public Page<RolePermissionVO> appsInRole(RolePermissionQuery query) {
		return baseMapper.appsInRole(new Page<RolePermissionEntity>(query.getPageIndex(), query.getPageSize()), query);
	}

	@Override
	public Page<RolePermissionVO> appsNotInRole(RolePermissionQuery query) {
		return baseMapper.appsNotInRole(new Page<RolePermissionEntity>(query.getPageIndex(), query.getPageSize()),
				query);
	}
}