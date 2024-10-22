package com.wy.test.web.mgt.contorller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.query.SyncQuery;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.SyncService;
import com.wy.test.sync.core.synchronizer.SyncProcessor;

import dream.flying.flower.lang.StrHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据同步
 *
 * @author 飞花梦影
 * @date 2024-10-22 23:18:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Tag(name = "数据同步API", description = "第三方数据同步API")
@RestController
@RequestMapping(value = { "/config/synchronizers" })
@Slf4j
public class SyncController {

	@Autowired
	SyncService synchronizersService;

	@GetMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> fetch(SyncQuery synchronizers, @CurrentUser UserEntity currentUser) {
		log.debug("" + synchronizers);
		synchronizers.setInstId(currentUser.getInstId());
		return new ResultResponse<>(synchronizersService.listPage(synchronizers)).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SyncEntity synchronizers = synchronizersService.getById(id);
		synchronizers.setCredentials(PasswordReciprocal.getInstance().decoder(synchronizers.getCredentials()));
		return new ResultResponse<SyncEntity>(synchronizers).buildResponse();
	}

	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody SyncEntity synchronizers, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + synchronizers);
		synchronizers.setInstId(currentUser.getInstId());
		synchronizers.setCredentials(PasswordReciprocal.getInstance().encode(synchronizers.getCredentials()));
		if (synchronizersService.updateById(synchronizers)) {
			return new ResultResponse<SyncEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<SyncEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@GetMapping(value = { "/synchr" })
	public ResponseEntity<?> synchr(@RequestParam("id") String id) {
		log.debug("-sync ids :" + id);

		String[] ids = StrHelper.split(id, ",");
		try {
			for (String sysId : ids) {
				SyncEntity synchronizer = synchronizersService.getById(sysId);
				synchronizer.setCredentials(PasswordReciprocal.getInstance().decoder(synchronizer.getCredentials()));
				log.debug("synchronizer " + synchronizer);
				SyncProcessor synchronizerService =
						AuthWebContext.getBean(synchronizer.getService(), SyncProcessor.class);
				if (synchronizerService != null) {
					synchronizerService.setSynchronizer(synchronizer);
					synchronizerService.sync();
				} else {
					log.info("synchronizer {} not exist .", synchronizer.getService());
				}
			}
		} catch (Exception e) {
			log.error("synchronizer Exception ", e);
			return new ResultResponse<SyncEntity>(ResultResponse.FAIL).buildResponse();

		}
		return new ResultResponse<SyncEntity>(ResultResponse.SUCCESS).buildResponse();
	}
}