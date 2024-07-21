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

import com.wy.test.common.entity.Message;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstsEntryType;
import com.wy.test.core.constants.ConstsOperateAction;
import com.wy.test.core.constants.ConstsOperateResult;
import com.wy.test.core.entity.Roles;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.RolesService;

import dream.flying.flower.lang.StrHelper;

@Controller
@RequestMapping(value = { "/access/roles" })
public class RolesController {

	final static Logger _logger = LoggerFactory.getLogger(RolesController.class);

	@Autowired
	RolesService rolesService;

	@Autowired
	HistorySystemLogsService systemLog;

	@GetMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute Roles role, @CurrentUser UserInfo currentUser) {
		_logger.debug("" + role);
		role.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<Roles>>(rolesService.fetchPageResults(role)).buildResponse();
	}

	@ResponseBody
	@GetMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute Roles role, @CurrentUser UserInfo currentUser) {
		_logger.debug("-query  :" + role);
		role.setInstId(currentUser.getInstId());
		if (CollectionUtils.isNotEmpty(rolesService.query(role))) {
			return new Message<Roles>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Roles>(Message.FAIL).buildResponse();
		}

	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id, @CurrentUser UserInfo currentUser) {
		Roles role = rolesService.get(id);
		return new Message<Roles>(role).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody Roles role, @CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + role);
		role.setInstId(currentUser.getInstId());
		role.setId(role.generateId());
		if (StrHelper.isBlank(role.getRoleCode())) {
			role.setRoleCode(role.getId());
		}
		if (rolesService.insert(role)) {
			rolesService.refreshDynamicRoles(role);
			systemLog.insert(ConstsEntryType.ROLE, role, ConstsOperateAction.CREATE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Roles>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Roles>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody Roles role, @CurrentUser UserInfo currentUser) {
		_logger.debug("-update  group :" + role);
		if (role.getId().equalsIgnoreCase("ROLE_ALL_USER")) {
			role.setDefaultAllUser();
		}
		role.setInstId(currentUser.getInstId());
		if (rolesService.update(role)) {
			rolesService.refreshDynamicRoles(role);
			systemLog.insert(ConstsEntryType.ROLE, role, ConstsOperateAction.UPDATE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Roles>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Roles>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete ids : {}", ids);
		ids = ids.replaceAll("ROLE_ALL_USER", "-1").replaceAll("ROLE_ADMINISTRATORS", "-1");
		if (rolesService.deleteBatch(ids)) {
			systemLog.insert(ConstsEntryType.ROLE, ids, ConstsOperateAction.DELETE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Roles>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Roles>(Message.FAIL).buildResponse();
		}
	}
}
