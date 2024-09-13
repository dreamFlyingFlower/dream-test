package com.wy.test.web.mgt.contorller.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.RolePrivilegeEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistorySysLogService;
import com.wy.test.persistence.service.RolePrivilegeService;

import dream.flying.flower.generator.GeneratorStrategyContext;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/permissions/privileges" })
@Slf4j
public class RolePrivilegeController {

	@Autowired
	RolePrivilegeService rolePrivilegesService;

	@Autowired
	HistorySysLogService systemLog;

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody RolePrivilegeEntity rolePrivileges,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  : " + rolePrivileges);
		// have
		RolePrivilegeEntity queryRolePrivileges =
				new RolePrivilegeEntity(rolePrivileges.getAppId(), rolePrivileges.getRoleId(), currentUser.getInstId());
		List<RolePrivilegeEntity> roleRolePrivilegesList =
				rolePrivilegesService.queryRolePrivileges(queryRolePrivileges);

		HashMap<String, String> privilegeMap = new HashMap<String, String>();
		for (RolePrivilegeEntity rolePrivilege : roleRolePrivilegesList) {
			privilegeMap.put(rolePrivilege.getUniqueId(), rolePrivilege.getId());
		}
		// Maybe insert
		ArrayList<RolePrivilegeEntity> newRolePrivilegesList = new ArrayList<RolePrivilegeEntity>();
		String[] resourceIds = StrHelper.split(rolePrivileges.getResourceId(), ",");
		HashMap<String, String> newPrivilegesMap = new HashMap<String, String>();
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		for (String resourceId : resourceIds) {
			RolePrivilegeEntity newRolePrivilege = new RolePrivilegeEntity(rolePrivileges.getAppId(),
					rolePrivileges.getRoleId(), resourceId, currentUser.getInstId());
			newRolePrivilege.setId(generatorStrategyContext.generate());
			newPrivilegesMap.put(newRolePrivilege.getUniqueId(), rolePrivileges.getAppId());

			if (!rolePrivileges.getAppId().equalsIgnoreCase(resourceId)
					&& !privilegeMap.containsKey(newRolePrivilege.getUniqueId())) {
				newRolePrivilegesList.add(newRolePrivilege);
			}
		}

		// delete
		ArrayList<RolePrivilegeEntity> deleteRolePrivilegesList = new ArrayList<RolePrivilegeEntity>();
		for (RolePrivilegeEntity rolePrivilege : roleRolePrivilegesList) {
			if (!newPrivilegesMap.containsKey(rolePrivilege.getUniqueId())) {
				rolePrivilege.setInstId(currentUser.getInstId());
				deleteRolePrivilegesList.add(rolePrivilege);
			}
		}
		if (!deleteRolePrivilegesList.isEmpty()) {
			log.debug("-remove  : " + deleteRolePrivilegesList);
			rolePrivilegesService.deleteRolePrivileges(deleteRolePrivilegesList);
		}

		if (!newRolePrivilegesList.isEmpty() && rolePrivilegesService.insertRolePrivileges(newRolePrivilegesList)) {
			log.debug("-insert  : " + newRolePrivilegesList);
			return new ResultResponse<RolePrivilegeEntity>(ResultResponse.SUCCESS).buildResponse();

		} else {
			return new ResultResponse<RolePrivilegeEntity>(ResultResponse.SUCCESS).buildResponse();
		}

	}

	@ResponseBody
	@PostMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@RequestBody RolePrivilegeEntity rolePrivileges, @CurrentUser UserEntity currentUser) {
		log.debug("-get  :" + rolePrivileges);
		// have
		RolePrivilegeEntity queryRolePrivilege =
				new RolePrivilegeEntity(rolePrivileges.getAppId(), rolePrivileges.getRoleId(), currentUser.getInstId());
		List<RolePrivilegeEntity> rolePrivilegeList = rolePrivilegesService.queryRolePrivileges(queryRolePrivilege);

		return new ResultResponse<List<RolePrivilegeEntity>>(rolePrivilegeList).buildResponse();
	}
}