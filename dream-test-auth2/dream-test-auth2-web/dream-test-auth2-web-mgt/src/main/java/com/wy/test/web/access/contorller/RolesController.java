package com.wy.test.web.access.contorller;

import org.apache.commons.collections4.CollectionUtils;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstEntryType;
import com.wy.test.core.constants.ConstOperateAction;
import com.wy.test.core.constants.ConstOperateResult;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.RoleEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.RolesService;

import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/access/roles" })
@Slf4j
public class RolesController {

	final static Logger log = LoggerFactory.getLogger(RolesController.class);

	@Autowired
	RolesService rolesService;

	@Autowired
	HistorySystemLogsService systemLog;

	@GetMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute RoleEntity role, @CurrentUser UserEntity currentUser) {
		log.debug("" + role);
		role.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<RoleEntity>>(rolesService.fetchPageResults(role)).buildResponse();
	}

	@ResponseBody
	@GetMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute RoleEntity role, @CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + role);
		role.setInstId(currentUser.getInstId());
		if (CollectionUtils.isNotEmpty(rolesService.query(role))) {
			return new Message<RoleEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<RoleEntity>(Message.FAIL).buildResponse();
		}

	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id, @CurrentUser UserEntity currentUser) {
		RoleEntity role = rolesService.get(id);
		return new Message<RoleEntity>(role).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody RoleEntity role, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + role);
		role.setInstId(currentUser.getInstId());
		role.setId(role.generateId());
		if (StrHelper.isBlank(role.getRoleCode())) {
			role.setRoleCode(role.getId());
		}
		if (rolesService.insert(role)) {
			rolesService.refreshDynamicRoles(role);
			systemLog.insert(ConstEntryType.ROLE, role, ConstOperateAction.CREATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<RoleEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<RoleEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody RoleEntity role, @CurrentUser UserEntity currentUser) {
		log.debug("-update  group :" + role);
		if (role.getId().equalsIgnoreCase("ROLE_ALL_USER")) {
			role.setDefaultAllUser();
		}
		role.setInstId(currentUser.getInstId());
		if (rolesService.update(role)) {
			rolesService.refreshDynamicRoles(role);
			systemLog.insert(ConstEntryType.ROLE, role, ConstOperateAction.UPDATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<RoleEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<RoleEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete ids : {}", ids);
		ids = ids.replaceAll("ROLE_ALL_USER", "-1").replaceAll("ROLE_ADMINISTRATORS", "-1");
		if (rolesService.deleteBatch(ids)) {
			systemLog.insert(ConstEntryType.ROLE, ids, ConstOperateAction.DELETE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<RoleEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<RoleEntity>(Message.FAIL).buildResponse();
		}
	}
}