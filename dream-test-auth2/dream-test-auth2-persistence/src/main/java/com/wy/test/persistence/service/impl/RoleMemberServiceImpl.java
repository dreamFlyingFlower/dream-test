package com.wy.test.persistence.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.test.core.convert.RoleMemberConvert;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.RoleMemberEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.RoleMemberQuery;
import com.wy.test.core.vo.RoleMemberVO;
import com.wy.test.persistence.mapper.RoleMemberMapper;
import com.wy.test.persistence.service.RoleMemberService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 角色成员
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class RoleMemberServiceImpl extends
		AbstractServiceImpl<RoleMemberEntity, RoleMemberVO, RoleMemberQuery, RoleMemberConvert, RoleMemberMapper>
		implements RoleMemberService {

	@Override
	public int addDynamicRoleMember(RoleEntity dynamicGroup) {
		return baseMapper.addDynamicRoleMember(dynamicGroup);
	}

	@Override
	public int deleteDynamicRoleMember(RoleEntity dynamicGroup) {
		return baseMapper.deleteDynamicRoleMember(dynamicGroup);
	}

	@Override
	public int deleteByRoleId(String groupId) {
		return baseMapper.deleteByRoleId(groupId);
	}

	@Override
	public List<UserEntity> queryMemberByRoleId(String groupId) {
		return baseMapper.queryMemberByRoleId(groupId);
	}

	@Override
	public Page<RoleEntity> rolesNoMember(RoleMemberQuery query) {
		return baseMapper.rolesNoMember(new Page<RoleEntity>(query.getPageIndex(), query.getPageSize()), query);
	}
}