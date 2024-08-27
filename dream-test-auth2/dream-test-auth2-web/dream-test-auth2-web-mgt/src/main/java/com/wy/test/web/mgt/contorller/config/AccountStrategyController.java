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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.query.AccountStrategyQuery;
import com.wy.test.core.vo.AccountStrategyVO;
import com.wy.test.persistence.service.AccountService;
import com.wy.test.persistence.service.AccountStrategyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/config/accountsstrategy" })
@Slf4j
public class AccountStrategyController {

	@Autowired
	AccountStrategyService accountsStrategyService;

	@Autowired
	AccountService accountsService;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@RequestBody AccountStrategyQuery accountsStrategy,
			@CurrentUser UserEntity currentUser) {
		accountsStrategy.setInstId(currentUser.getInstId());
		Page<AccountStrategyVO> page = accountsStrategyService.page(accountsStrategy);
		for (AccountStrategyVO strategy : page.getRecords()) {
			strategy.transIconBase64();
		}
		log.debug("Accounts Strategy " + page);
		return new Message<>(page).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@RequestBody AccountStrategyEntity accountsStrategy,
			@CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + accountsStrategy);
		if (CollectionUtils.isNotEmpty(accountsStrategyService.list(accountsStrategy))) {
			return new Message<AccountStrategyEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountStrategyEntity>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AccountStrategyEntity accountsStrategy = accountsStrategyService.getById(id);
		return new Message<AccountStrategyEntity>(accountsStrategy).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AccountStrategyEntity accountsStrategy,
			@CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + accountsStrategy);

		if (accountsStrategyService.save(accountsStrategy)) {
			accountsService.refreshByStrategy(accountsStrategy);
			return new Message<AccountStrategyEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountStrategyEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AccountStrategyEntity accountsStrategy,
			@CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + accountsStrategy);
		if (accountsStrategyService.updateById(accountsStrategy)) {
			accountsService.refreshByStrategy(accountsStrategy);
			return new Message<AccountStrategyEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountStrategyEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (accountsStrategyService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<AccountStrategyEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountStrategyEntity>(Message.FAIL).buildResponse();
		}
	}
}