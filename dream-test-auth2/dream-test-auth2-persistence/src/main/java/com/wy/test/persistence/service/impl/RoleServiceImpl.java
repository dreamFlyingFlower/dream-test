package com.wy.test.persistence.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.convert.RoleConvert;
import com.wy.test.core.entity.InstitutionEntity;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.enums.RoleCategory;
import com.wy.test.core.query.RoleQuery;
import com.wy.test.core.vo.RoleVO;
import com.wy.test.persistence.mapper.RoleMapper;
import com.wy.test.persistence.service.InstitutionService;
import com.wy.test.persistence.service.RoleMemberService;
import com.wy.test.persistence.service.RoleService;

import dream.flying.flower.db.SqlHelper;
import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.flying.flower.lang.StrHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@Slf4j
@AllArgsConstructor
public class RoleServiceImpl extends AbstractServiceImpl<RoleEntity, RoleVO, RoleQuery, RoleConvert, RoleMapper>
		implements RoleService {

	private final RoleMemberService roleMemberService;

	private final InstitutionService institutionService;

	@Override
	public List<RoleEntity> queryDynamicRoles(RoleEntity groups) {
		return this.baseMapper.queryDynamicRoles(groups);
	}

	@Override
	public boolean deleteById(String groupId) {
		this.removeById(groupId);
		roleMemberService.deleteByRoleId(groupId);
		return true;
	}

	@Override
	public List<RoleEntity> queryRolesByUserId(String userId) {
		return this.baseMapper.queryRolesByUserId(userId);
	}

	@Override
	public void refreshDynamicRoles(RoleEntity dynamicRole) {
		if (dynamicRole.getCategory().equalsIgnoreCase(RoleCategory.DYNAMIC.name())) {
			boolean isDynamicTimeSupport = false;
			boolean isBetweenEffectiveTime = false;
			if (StrHelper.isNotBlank(dynamicRole.getResumeTime()) && StrHelper.isNotBlank(dynamicRole.getSuspendTime())
					&& !dynamicRole.getSuspendTime().equals("00:00")) {
				LocalTime currentTime = LocalDateTime.now().toLocalTime();
				LocalTime resumeTime = LocalTime.parse(dynamicRole.getResumeTime());
				LocalTime suspendTime = LocalTime.parse(dynamicRole.getSuspendTime());

				log.info("currentTime: " + currentTime + " , resumeTime : " + resumeTime + " , suspendTime: "
						+ suspendTime);
				isDynamicTimeSupport = true;

				if (resumeTime.isBefore(currentTime) && currentTime.isBefore(suspendTime)) {
					isBetweenEffectiveTime = true;
				}

			}

			if (StrHelper.isNotBlank(dynamicRole.getOrgIdsList())) {
				String[] orgIds = dynamicRole.getOrgIdsList().split(",");
				StringBuffer orgIdFilters = new StringBuffer();
				for (String orgId : orgIds) {
					if (StrHelper.isNotBlank(orgId)) {
						if (orgIdFilters.length() > 0) {
							orgIdFilters.append(",");
						}
						orgIdFilters.append("'").append(orgId).append("'");
					}
				}
				if (orgIdFilters.length() > 0) {
					dynamicRole.setOrgIdsList(orgIdFilters.toString());
				}
			}

			String filters = dynamicRole.getFilters();
			if (StrHelper.isNotBlank(filters)) {
				if (SqlHelper.filtersSQLInjection(filters.toLowerCase())) {
					log.info("filters include SQL Injection Attack Risk.");
					return;
				}
				filters = filters.replace("&", " AND ");
				filters = filters.replace("|", " OR ");

				dynamicRole.setFilters(filters);
			}

			if (isDynamicTimeSupport) {
				if (isBetweenEffectiveTime) {
					roleMemberService.deleteDynamicRoleMember(dynamicRole);
					roleMemberService.addDynamicRoleMember(dynamicRole);
				} else {
					roleMemberService.deleteDynamicRoleMember(dynamicRole);
				}
			} else {
				roleMemberService.deleteDynamicRoleMember(dynamicRole);
				roleMemberService.addDynamicRoleMember(dynamicRole);
			}
		}
	}

	@Override
	public void refreshAllDynamicRoles() {
		List<InstitutionEntity> instList = institutionService
				.list(new LambdaQueryWrapper<InstitutionEntity>().eq(InstitutionEntity::getStatus, ConstStatus.ACTIVE));
		for (InstitutionEntity inst : instList) {
			RoleEntity role = new RoleEntity();
			role.setInstId(inst.getId());
			List<RoleEntity> rolesList = queryDynamicRoles(role);
			for (RoleEntity r : rolesList) {
				log.debug("role " + rolesList);
				refreshDynamicRoles(r);
			}
		}
	}
}