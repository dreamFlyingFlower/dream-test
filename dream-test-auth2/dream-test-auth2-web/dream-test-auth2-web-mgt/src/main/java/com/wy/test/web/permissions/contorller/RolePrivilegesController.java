package com.wy.test.web.permissions.contorller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.RolePrivilegeEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.RolePrivilegesService;

import dream.flying.flower.lang.StrHelper;

@Controller
@RequestMapping(value = { "/permissions/privileges" })
public class RolePrivilegesController {

	final static Logger _logger = LoggerFactory.getLogger(RolePrivilegesController.class);

	@Autowired
	RolePrivilegesService rolePrivilegesService;

	@Autowired
	HistorySystemLogsService systemLog;

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody RolePrivilegeEntity rolePrivileges, @CurrentUser UserEntity currentUser) {
		_logger.debug("-update  : " + rolePrivileges);
		// have
		RolePrivilegeEntity queryRolePrivileges =
				new RolePrivilegeEntity(rolePrivileges.getAppId(), rolePrivileges.getRoleId(), currentUser.getInstId());
		List<RolePrivilegeEntity> roleRolePrivilegesList = rolePrivilegesService.queryRolePrivileges(queryRolePrivileges);

		HashMap<String, String> privilegeMap = new HashMap<String, String>();
		for (RolePrivilegeEntity rolePrivilege : roleRolePrivilegesList) {
			privilegeMap.put(rolePrivilege.getUniqueId(), rolePrivilege.getId());
		}
		// Maybe insert
		ArrayList<RolePrivilegeEntity> newRolePrivilegesList = new ArrayList<RolePrivilegeEntity>();
		String[] resourceIds = StrHelper.split(rolePrivileges.getResourceId(), ",");
		HashMap<String, String> newPrivilegesMap = new HashMap<String, String>();
		for (String resourceId : resourceIds) {
			RolePrivilegeEntity newRolePrivilege = new RolePrivilegeEntity(rolePrivileges.getAppId(), rolePrivileges.getRoleId(),
					resourceId, currentUser.getInstId());
			newRolePrivilege.setId(newRolePrivilege.generateId());
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
			_logger.debug("-remove  : " + deleteRolePrivilegesList);
			rolePrivilegesService.deleteRolePrivileges(deleteRolePrivilegesList);
		}

		if (!newRolePrivilegesList.isEmpty() && rolePrivilegesService.insertRolePrivileges(newRolePrivilegesList)) {
			_logger.debug("-insert  : " + newRolePrivilegesList);
			return new Message<RolePrivilegeEntity>(Message.SUCCESS).buildResponse();

		} else {
			return new Message<RolePrivilegeEntity>(Message.SUCCESS).buildResponse();
		}

	}

	@ResponseBody
	@PostMapping(value = { "/get" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@ModelAttribute RolePrivilegeEntity rolePrivileges, @CurrentUser UserEntity currentUser) {
		_logger.debug("-get  :" + rolePrivileges);
		// have
		RolePrivilegeEntity queryRolePrivilege =
				new RolePrivilegeEntity(rolePrivileges.getAppId(), rolePrivileges.getRoleId(), currentUser.getInstId());
		List<RolePrivilegeEntity> rolePrivilegeList = rolePrivilegesService.queryRolePrivileges(queryRolePrivilege);

		return new Message<List<RolePrivilegeEntity>>(rolePrivilegeList).buildResponse();
	}
}