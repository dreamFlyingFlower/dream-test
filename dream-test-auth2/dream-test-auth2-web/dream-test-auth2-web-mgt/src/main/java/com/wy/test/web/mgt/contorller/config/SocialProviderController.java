package com.wy.test.web.mgt.contorller.config;

import java.util.Arrays;

import org.apache.commons.collections4.CollectionUtils;
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

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.SocialProviderEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.query.SocialProviderQuery;
import com.wy.test.persistence.service.SocialProviderService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/socialsprovider" })
@Slf4j
public class SocialProviderController {

	@Autowired
	SocialProviderService socialsProviderService;

	@GetMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(SocialProviderQuery socialsProvider, @CurrentUser UserEntity currentUser) {
		log.debug("" + socialsProvider);
		socialsProvider.setInstId(currentUser.getInstId());
		return new Message<>(socialsProviderService.listPage(socialsProvider)).buildResponse();
	}

	@ResponseBody
	@GetMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(SocialProviderEntity socialsProvider, @CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + socialsProvider);
		socialsProvider.setInstId(currentUser.getInstId());
		if (CollectionUtils.isNotEmpty(socialsProviderService.list(socialsProvider))) {
			return new Message<>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SocialProviderEntity socialsProvider = socialsProviderService.getById(id);
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().decoder(socialsProvider.getClientSecret()));
		return new Message<>(socialsProvider).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody SocialProviderEntity socialsProvider,
			@CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + socialsProvider);
		socialsProvider.setInstId(currentUser.getInstId());
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().encode(socialsProvider.getClientSecret()));
		if (socialsProviderService.save(socialsProvider)) {
			return new Message<>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody SocialProviderEntity socialsProvider,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + socialsProvider);
		socialsProvider.setInstId(currentUser.getInstId());
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().encode(socialsProvider.getClientSecret()));
		if (socialsProviderService.updateById(socialsProvider)) {
			return new Message<>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (socialsProviderService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.FAIL).buildResponse();
		}
	}
}