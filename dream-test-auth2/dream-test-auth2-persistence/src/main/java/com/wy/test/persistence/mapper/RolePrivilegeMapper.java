package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.RolePrivilegeEntity;
import com.wy.test.core.query.RolePrivilegeQuery;
import com.wy.test.core.vo.RolePrivilegeVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 角色权限
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RolePrivilegeMapper extends BaseMappers<RolePrivilegeEntity, RolePrivilegeVO, RolePrivilegeQuery> {

	int insertRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList);

	int deleteRolePrivileges(List<RolePrivilegeEntity> rolePermissionsList);

	List<RolePrivilegeEntity> queryRolePrivileges(RolePrivilegeEntity rolePermissions);
}