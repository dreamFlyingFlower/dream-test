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
import com.wy.test.core.entity.AppAdapterEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.AppAdapterQuery;
import com.wy.test.persistence.service.AppAdapterService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/adapters" })
@Slf4j
public class AdapterController {

	@Autowired
	AppAdapterService appsAdaptersService;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@RequestBody AppAdapterQuery appsAdapter) {
		log.debug("" + appsAdapter);
		return new Message<>(appsAdaptersService.listPage(appsAdapter)).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@RequestBody AppAdapterQuery appsAdapter, @CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + appsAdapter);
		if (CollectionUtils.isNotEmpty(appsAdaptersService.list(appsAdapter))) {
			return new Message<AppAdapterEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppAdapterEntity>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppAdapterEntity appsAdapter = appsAdaptersService.getById(id);
		return new Message<AppAdapterEntity>(appsAdapter).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AppAdapterEntity appsAdapter, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + appsAdapter);
		if (appsAdaptersService.save(appsAdapter)) {
			return new Message<AppAdapterEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppAdapterEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppAdapterEntity appsAdapter, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + appsAdapter);
		if (appsAdaptersService.updateById(appsAdapter)) {
			return new Message<AppAdapterEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppAdapterEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (appsAdaptersService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<AppAdapterEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppAdapterEntity>(Message.FAIL).buildResponse();
		}
	}
}