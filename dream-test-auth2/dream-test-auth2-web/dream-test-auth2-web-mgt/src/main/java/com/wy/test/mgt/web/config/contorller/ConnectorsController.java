package com.wy.test.mgt.web.config.contorller;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.ConnectorEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.query.ConnectorQuery;
import com.wy.test.core.vo.ConnectorVO;
import com.wy.test.persistence.service.ConnectorService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/connectors" })
@Slf4j
public class ConnectorsController {

	@Autowired
	ConnectorService connectorsService;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(ConnectorQuery connector, @CurrentUser UserEntity currentUser) {
		log.debug("" + connector);
		connector.setInstId(currentUser.getInstId());
		return new Message<>(connectorsService.listPage(connector)).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		ConnectorVO connector = connectorsService.getInfo(id);
		if (StringUtils.isNotBlank(connector.getCredentials())) {
			connector.setCredentials(PasswordReciprocal.getInstance().decoder(connector.getCredentials()));
		}
		return new Message<>(connector).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody ConnectorEntity connector, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + connector);
		connector.setInstId(currentUser.getInstId());
		if (StringUtils.isNotBlank(connector.getCredentials())) {
			connector.setCredentials(PasswordReciprocal.getInstance().encode(connector.getCredentials()));
		}
		if (connectorsService.save(connector)) {
			return new Message<ConnectorEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ConnectorEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody ConnectorEntity connector, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + connector);
		connector.setInstId(currentUser.getInstId());
		connector.setCredentials(PasswordReciprocal.getInstance().encode(connector.getCredentials()));
		if (connectorsService.updateById(connector)) {
			return new Message<ConnectorEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ConnectorEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (connectorsService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<ConnectorEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ConnectorEntity>(Message.FAIL).buildResponse();
		}
	}
}