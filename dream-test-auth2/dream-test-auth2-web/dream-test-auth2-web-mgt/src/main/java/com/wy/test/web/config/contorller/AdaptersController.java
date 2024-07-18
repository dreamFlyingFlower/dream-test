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
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.AppsAdapters;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.AppsAdaptersService;

@Controller
@RequestMapping(value = { "/config/adapters" })
public class AdaptersController {

	final static Logger _logger = LoggerFactory.getLogger(AdaptersController.class);

	@Autowired
	AppsAdaptersService appsAdaptersService;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute AppsAdapters appsAdapter) {
		_logger.debug("" + appsAdapter);
		return new Message<JpaPageResults<AppsAdapters>>(appsAdaptersService.fetchPageResults(appsAdapter))
				.buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute AppsAdapters appsAdapter, @CurrentUser UserInfo currentUser) {
		_logger.debug("-query  :" + appsAdapter);
		if (CollectionUtils.isNotEmpty(appsAdaptersService.query(appsAdapter))) {
			return new Message<AppsAdapters>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsAdapters>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppsAdapters appsAdapter = appsAdaptersService.get(id);
		return new Message<AppsAdapters>(appsAdapter).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AppsAdapters appsAdapter, @CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + appsAdapter);

		if (appsAdaptersService.insert(appsAdapter)) {
			return new Message<AppsAdapters>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsAdapters>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppsAdapters appsAdapter, @CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + appsAdapter);
		if (appsAdaptersService.update(appsAdapter)) {
			return new Message<AppsAdapters>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsAdapters>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (appsAdaptersService.deleteBatch(ids)) {
			return new Message<AppsAdapters>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppsAdapters>(Message.FAIL).buildResponse();
		}
	}
}
