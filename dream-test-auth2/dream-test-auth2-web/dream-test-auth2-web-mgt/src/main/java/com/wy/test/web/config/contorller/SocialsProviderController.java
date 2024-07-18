package com.wy.test.web.config.contorller;

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
import com.wy.test.core.entity.SocialsProvider;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.SocialsProviderService;

@Controller
@RequestMapping(value = { "/config/socialsprovider" })
public class SocialsProviderController {

	final static Logger _logger = LoggerFactory.getLogger(SocialsProviderController.class);

	@Autowired
	SocialsProviderService socialsProviderService;

	@GetMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute SocialsProvider socialsProvider, @CurrentUser UserInfo currentUser) {
		_logger.debug("" + socialsProvider);
		socialsProvider.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<SocialsProvider>>(socialsProviderService.fetchPageResults(socialsProvider))
				.buildResponse();
	}

	@ResponseBody
	@GetMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute SocialsProvider socialsProvider, @CurrentUser UserInfo currentUser) {
		_logger.debug("-query  :" + socialsProvider);
		socialsProvider.setInstId(currentUser.getInstId());
		if (CollectionUtils.isNotEmpty(socialsProviderService.query(socialsProvider))) {
			return new Message<SocialsProvider>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<SocialsProvider>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		SocialsProvider socialsProvider = socialsProviderService.get(id);
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().decoder(socialsProvider.getClientSecret()));
		return new Message<SocialsProvider>(socialsProvider).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody SocialsProvider socialsProvider, @CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + socialsProvider);
		socialsProvider.setInstId(currentUser.getInstId());
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().encode(socialsProvider.getClientSecret()));
		if (socialsProviderService.insert(socialsProvider)) {
			return new Message<SocialsProvider>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<SocialsProvider>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody SocialsProvider socialsProvider, @CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + socialsProvider);
		socialsProvider.setInstId(currentUser.getInstId());
		socialsProvider.setClientSecret(PasswordReciprocal.getInstance().encode(socialsProvider.getClientSecret()));
		if (socialsProviderService.update(socialsProvider)) {
			return new Message<SocialsProvider>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<SocialsProvider>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (socialsProviderService.deleteBatch(ids)) {
			return new Message<SocialsProvider>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<SocialsProvider>(Message.FAIL).buildResponse();
		}
	}

}
