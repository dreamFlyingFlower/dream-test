package com.wy.test.web.mgt.contorller.access;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.RolePermissionEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.RolePermissionQuery;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.RolePermissionVO;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.HistorySysLogService;
import com.wy.test.persistence.service.RolePermissionService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/access/permissions" })
@Slf4j
public class RolePermissionController {

	@Autowired
	RolePermissionService rolePermissionssService;

	@Autowired
	HistorySysLogService systemLog;

	@GetMapping(value = { "/appsInRole" })
	@ResponseBody
	public ResponseEntity<?> appsInRole(RolePermissionQuery rolePermission, @CurrentUser UserEntity currentUser) {
		rolePermission.setInstId(currentUser.getInstId());
		Page<RolePermissionVO> rolePermissions = rolePermissionssService.appsInRole(rolePermission);
		if (rolePermissions != null && rolePermissions.getRecords() != null) {
			for (AppVO app : rolePermissions.getRecords()) {
				app.transIconBase64();
			}
		}
		return new Message<>(Message.FAIL, rolePermissions).buildResponse();
	}

	@GetMapping(value = { "/appsNotInRole" })
	@ResponseBody
	public ResponseEntity<?> appsNotInRole(@RequestBody RolePermissionQuery rolePermission,
			@CurrentUser UserEntity currentUser) {
		rolePermission.setInstId(currentUser.getInstId());
		Page<RolePermissionVO> rolePermissions = rolePermissionssService.appsNotInRole(rolePermission);
		if (rolePermissions != null && rolePermissions.getRecords() != null) {
			for (AppVO app : rolePermissions.getRecords()) {
				app.transIconBase64();
			}
		}
		return new Message<>(Message.FAIL, rolePermissions).buildResponse();
	}

	@PostMapping(value = { "/add" })
	@ResponseBody
	public ResponseEntity<?> insertPermission(@RequestBody RolePermissionEntity rolePermission,
			@CurrentUser UserEntity currentUser) {
		if (rolePermission == null || rolePermission.getRoleId() == null) {
			return new Message<RolePermissionEntity>(Message.FAIL).buildResponse();
		}
		String roleId = rolePermission.getRoleId();

		boolean result = true;
		String appIds = rolePermission.getAppId();
		if (appIds != null) {
			String[] arrAppIds = appIds.split(",");
			for (int i = 0; i < arrAppIds.length; i++) {
				RolePermissionEntity newRolePermissions =
						new RolePermissionEntity(roleId, arrAppIds[i], currentUser.getInstId());
				newRolePermissions.setId(WebContext.genId());
				result = rolePermissionssService.save(newRolePermissions);
			}
			if (result) {
				return new Message<RolePermissionEntity>(Message.SUCCESS).buildResponse();
			}
		}
		return new Message<RolePermissionEntity>(Message.FAIL).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete ids : {}", ids);
		if (rolePermissionssService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<RolePermissionEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<RolePermissionEntity>(Message.FAIL).buildResponse();
		}
	}
}