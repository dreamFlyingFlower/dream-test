package com.wy.test.persistence.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.RoleMemberEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.RoleMemberQuery;
import com.wy.test.core.vo.RoleMemberVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 角色成员
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface RoleMemberService extends BaseServices<RoleMemberEntity, RoleMemberVO, RoleMemberQuery> {

	int addDynamicRoleMember(RoleEntity dynamicGroup);

	int deleteDynamicRoleMember(RoleEntity dynamicGroup);

	int deleteByRoleId(String groupId);

	List<UserEntity> queryMemberByRoleId(String groupId);

	Page<RoleEntity> rolesNoMember(RoleMemberQuery query);
}