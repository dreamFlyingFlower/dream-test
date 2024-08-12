package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.query.RoleQuery;
import com.wy.test.core.vo.RoleVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 角色
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RoleMapper extends BaseMappers<RoleEntity, RoleVO, RoleQuery> {

	List<RoleEntity> queryRolesByUserId(String userId);
}