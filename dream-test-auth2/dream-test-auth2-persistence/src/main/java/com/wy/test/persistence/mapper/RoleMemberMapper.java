package com.wy.test.persistence.mapper;

import java.util.List;

import org.dromara.mybatis.jpa.IJpaMapper;

import com.wy.test.entity.RoleMember;
import com.wy.test.entity.Roles;
import com.wy.test.entity.UserInfo;

public interface RoleMemberMapper extends IJpaMapper<RoleMember> {

	public List<RoleMember> memberInRole(RoleMember entity);

	public List<RoleMember> memberNotInRole(RoleMember entity);

	public List<Roles> rolesNoMember(RoleMember entity);

	public int addDynamicRoleMember(Roles dynamicRole);

	public int deleteDynamicRoleMember(Roles dynamicRole);

	public int deleteByRoleId(String roleId);

	public List<UserInfo> queryMemberByRoleId(String roleId);

}
