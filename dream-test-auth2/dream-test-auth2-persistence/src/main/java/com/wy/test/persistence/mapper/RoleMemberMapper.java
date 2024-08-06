package com.wy.test.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.RoleMemberEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.RoleMemberQuery;
import com.wy.test.core.vo.RoleMemberVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 角色成员
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RoleMemberMapper extends BaseMappers<RoleMemberEntity, RoleMemberVO, RoleMemberQuery> {

	List<RoleMemberEntity> memberInRole(RoleMemberQuery entity);

	List<RoleMemberEntity> memberNotInRole(RoleMemberQuery entity);

	Page<RoleEntity> rolesNoMember(Page<RoleEntity> page, RoleMemberQuery query);

	int addDynamicRoleMember(RoleEntity dynamicRole);

	int deleteDynamicRoleMember(RoleEntity dynamicRole);

	int deleteByRoleId(String roleId);

	List<UserEntity> queryMemberByRoleId(String roleId);
}