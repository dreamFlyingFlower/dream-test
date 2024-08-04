package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.RolePermissionEntity;
import com.wy.test.core.query.RolePermissionQuery;
import com.wy.test.core.vo.RolePermissionVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RolePermissionMapper extends BaseMappers<RolePermissionEntity, RolePermissionVO, RolePermissionQuery> {

	List<RolePermissionEntity> appsInRole(RolePermissionEntity entity);

	List<RolePermissionEntity> appsNotInRole(RolePermissionEntity entity);
}