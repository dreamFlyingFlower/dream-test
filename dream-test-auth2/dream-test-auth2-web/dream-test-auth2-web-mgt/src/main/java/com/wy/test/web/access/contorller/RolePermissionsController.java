package com.wy.test.web.access.contorller;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.RolePermissions;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.RolePermissionssService;

@Controller
@RequestMapping(value = { "/access/permissions" })
public class RolePermissionsController {

	final static Logger _logger = LoggerFactory.getLogger(RolePermissionsController.class);

	@Autowired
	RolePermissionssService rolePermissionssService;

	@Autowired
	HistorySystemLogsService systemLog;

	@GetMapping(value = { "/appsInRole" })
	@ResponseBody
	public ResponseEntity<?> appsInRole(@ModelAttribute RolePermissions rolePermission,
			@CurrentUser UserInfo currentUser) {
		JpaPageResults<RolePermissions> rolePermissions;
		rolePermission.setInstId(currentUser.getInstId());
		rolePermissions = rolePermissionssService.fetchPageResults("appsInRole", rolePermission);

		if (rolePermissions != null && rolePermissions.getRows() != null) {
			for (Apps app : rolePermissions.getRows()) {
				app.transIconBase64();
			}
		}
		return new Message<JpaPageResults<RolePermissions>>(Message.FAIL, rolePermissions).buildResponse();
	}

	@GetMapping(value = { "/appsNotInRole" })
	@ResponseBody
	public ResponseEntity<?> appsNotInRole(@ModelAttribute RolePermissions rolePermission,
			@CurrentUser UserInfo currentUser) {
		JpaPageResults<RolePermissions> rolePermissions;
		rolePermission.setInstId(currentUser.getInstId());
		rolePermissions = rolePermissionssService.fetchPageResults("appsNotInRole", rolePermission);

		if (rolePermissions != null && rolePermissions.getRows() != null) {
			for (Apps app : rolePermissions.getRows()) {
				app.transIconBase64();
			}
		}
		return new Message<JpaPageResults<RolePermissions>>(Message.FAIL, rolePermissions).buildResponse();
	}

	@PostMapping(value = { "/add" })
	@ResponseBody
	public ResponseEntity<?> insertPermission(@RequestBody RolePermissions rolePermission,
			@CurrentUser UserInfo currentUser) {
		if (rolePermission == null || rolePermission.getRoleId() == null) {
			return new Message<RolePermissions>(Message.FAIL).buildResponse();
		}
		String roleId = rolePermission.getRoleId();

		boolean result = true;
		String appIds = rolePermission.getAppId();
		if (appIds != null) {
			appIds = appIds.replaceAll("^,|,$", "");
			String[] arrAppIds = appIds.split(",");
			for (int i = 0; i < arrAppIds.length; i++) {
				RolePermissions newRolePermissions = new RolePermissions(roleId, arrAppIds[i], currentUser.getInstId());
				newRolePermissions.setId(WebContext.genId());
				result = rolePermissionssService.insert(newRolePermissions);
			}
			if (result) {
				return new Message<RolePermissions>(Message.SUCCESS).buildResponse();
			}
		}
		return new Message<RolePermissions>(Message.FAIL).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete ids : {}", ids);
		if (rolePermissionssService.deleteBatch(ids)) {
			return new Message<RolePermissions>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<RolePermissions>(Message.FAIL).buildResponse();
		}
	}

}
