package com.wy.test.web.config.contorller;

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
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.SynchronizersService;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import dream.flying.flower.lang.StrHelper;

@Controller
@RequestMapping(value = { "/config/synchronizers" })
public class SynchronizersController {

	final static Logger _logger = LoggerFactory.getLogger(SynchronizersController.class);

	@Autowired
	SynchronizersService synchronizersService;

	@GetMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(SyncEntity synchronizers, @CurrentUser UserEntity currentUser) {
		_logger.debug("" + synchronizers);
		synchronizers.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<SyncEntity>>(synchronizersService.fetchPageResults(synchronizers))
				.buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SyncEntity synchronizers = synchronizersService.get(id);
		synchronizers.setCredentials(PasswordReciprocal.getInstance().decoder(synchronizers.getCredentials()));
		return new Message<SyncEntity>(synchronizers).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody SyncEntity synchronizers, @CurrentUser UserEntity currentUser) {
		_logger.debug("-update  :" + synchronizers);
		synchronizers.setInstId(currentUser.getInstId());
		synchronizers.setCredentials(PasswordReciprocal.getInstance().encode(synchronizers.getCredentials()));
		if (synchronizersService.update(synchronizers)) {
			return new Message<SyncEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<SyncEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@GetMapping(value = { "/synchr" })
	public ResponseEntity<?> synchr(@RequestParam("id") String id) {
		_logger.debug("-sync ids :" + id);

		String[] ids = StrHelper.split(id, ",");
		try {
			for (String sysId : ids) {
				SyncEntity synchronizer = synchronizersService.get(sysId);
				synchronizer.setCredentials(PasswordReciprocal.getInstance().decoder(synchronizer.getCredentials()));
				_logger.debug("synchronizer " + synchronizer);
				ISynchronizerService synchronizerService =
						WebContext.getBean(synchronizer.getService(), ISynchronizerService.class);
				if (synchronizerService != null) {
					synchronizerService.setSynchronizer(synchronizer);
					synchronizerService.sync();
				} else {
					_logger.info("synchronizer {} not exist .", synchronizer.getService());
				}
			}
		} catch (Exception e) {
			_logger.error("synchronizer Exception ", e);
			return new Message<SyncEntity>(Message.FAIL).buildResponse();

		}
		return new Message<SyncEntity>(Message.SUCCESS).buildResponse();
	}

}
