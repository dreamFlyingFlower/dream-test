package com.wy.test.persistence.service;

import java.util.List;

import org.dromara.mybatis.jpa.JpaService;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.wy.test.core.entity.RoleMember;
import com.wy.test.core.entity.Roles;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.mapper.RoleMemberMapper;

@Repository
public class RoleMemberService extends JpaService<RoleMember> {

	final static Logger _logger = LoggerFactory.getLogger(RoleMemberService.class);

	public RoleMemberService() {
		super(RoleMemberMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public RoleMemberMapper getMapper() {
		return (RoleMemberMapper) super.getMapper();
	}

	public int addDynamicRoleMember(Roles dynamicGroup) {
		return getMapper().addDynamicRoleMember(dynamicGroup);
	}

	public int deleteDynamicRoleMember(Roles dynamicGroup) {
		return getMapper().deleteDynamicRoleMember(dynamicGroup);
	}

	public int deleteByRoleId(String groupId) {
		return getMapper().deleteByRoleId(groupId);
	}

	public List<UserInfo> queryMemberByRoleId(String groupId) {
		return getMapper().queryMemberByRoleId(groupId);
	}

	public JpaPageResults<Roles> rolesNoMember(RoleMember entity) {
		entity.setPageResultSelectUUID(entity.generateId());
		entity.setStartRow(calculateStartRow(entity.getPageNumber(), entity.getPageSize()));

		entity.setPageable(true);
		List<Roles> resultslist = null;
		try {
			resultslist = getMapper().rolesNoMember(entity);
		} catch (Exception e) {
			_logger.error("queryPageResults Exception ", e);
		}
		entity.setPageable(false);
		Integer totalPage = resultslist.size();

		Integer totalCount = 0;
		if (entity.getPageNumber() == 1 && totalPage < entity.getPageSize()) {
			totalCount = totalPage;
		} else {
			totalCount = parseCount(getMapper().fetchCount(entity));
		}

		return new JpaPageResults<Roles>(entity.getPageNumber(), entity.getPageSize(), totalPage, totalCount,
				resultslist);
	}

}
