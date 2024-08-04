package com.wy.test.web.config.contorller;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.wy.test.persistence.service.ConnectorsService;

@Controller
@RequestMapping(value = { "/config/connectors" })
public class ConnectorsController {

	final static Logger _logger = LoggerFactory.getLogger(ConnectorsController.class);

	@Autowired
	ConnectorsService connectorsService;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(ConnectorEntity connector, @CurrentUser UserEntity currentUser) {
		_logger.debug("" + connector);
		connector.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<ConnectorEntity>>(connectorsService.fetchPageResults(connector)).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		ConnectorEntity connector = connectorsService.get(id);
		if (StringUtils.isNotBlank(connector.getCredentials())) {
			connector.setCredentials(PasswordReciprocal.getInstance().decoder(connector.getCredentials()));
		}
		return new Message<ConnectorEntity>(connector).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody ConnectorEntity connector, @CurrentUser UserEntity currentUser) {
		_logger.debug("-Add  :" + connector);
		connector.setInstId(currentUser.getInstId());
		if (StringUtils.isNotBlank(connector.getCredentials())) {
			connector.setCredentials(PasswordReciprocal.getInstance().encode(connector.getCredentials()));
		}
		if (connectorsService.insert(connector)) {
			return new Message<ConnectorEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ConnectorEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody ConnectorEntity connector, @CurrentUser UserEntity currentUser) {
		_logger.debug("-update  :" + connector);
		connector.setInstId(currentUser.getInstId());
		connector.setCredentials(PasswordReciprocal.getInstance().encode(connector.getCredentials()));
		if (connectorsService.update(connector)) {
			return new Message<ConnectorEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ConnectorEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (connectorsService.deleteBatch(ids)) {
			return new Message<ConnectorEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<ConnectorEntity>(Message.FAIL).buildResponse();
		}
	}

}
