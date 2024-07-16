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
import com.wy.test.entity.AccountsStrategy;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.persistence.service.AccountsService;
import com.wy.test.persistence.service.AccountsStrategyService;

@Controller
@RequestMapping(value = { "/config/accountsstrategy" })
public class AccountsStrategyController {

	final static Logger _logger = LoggerFactory.getLogger(AccountsStrategyController.class);

	@Autowired
	AccountsStrategyService accountsStrategyService;

	@Autowired
	AccountsService accountsService;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute AccountsStrategy accountsStrategy,
			@CurrentUser UserInfo currentUser) {
		accountsStrategy.setInstId(currentUser.getInstId());
		JpaPageResults<AccountsStrategy> accountsStrategyList =
				accountsStrategyService.fetchPageResults(accountsStrategy);
		for (AccountsStrategy strategy : accountsStrategyList.getRows()) {
			strategy.transIconBase64();
		}
		_logger.debug("Accounts Strategy " + accountsStrategyList);
		return new Message<JpaPageResults<AccountsStrategy>>(accountsStrategyList).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute AccountsStrategy accountsStrategy,
			@CurrentUser UserInfo currentUser) {
		_logger.debug("-query  :" + accountsStrategy);
		if (CollectionUtils.isNotEmpty(accountsStrategyService.query(accountsStrategy))) {
			return new Message<AccountsStrategy>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountsStrategy>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AccountsStrategy accountsStrategy = accountsStrategyService.get(id);
		return new Message<AccountsStrategy>(accountsStrategy).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AccountsStrategy accountsStrategy, @CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + accountsStrategy);

		if (accountsStrategyService.insert(accountsStrategy)) {
			accountsService.refreshByStrategy(accountsStrategy);
			return new Message<AccountsStrategy>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountsStrategy>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AccountsStrategy accountsStrategy, @CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + accountsStrategy);
		if (accountsStrategyService.update(accountsStrategy)) {
			accountsService.refreshByStrategy(accountsStrategy);
			return new Message<AccountsStrategy>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountsStrategy>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (accountsStrategyService.deleteBatch(ids)) {
			return new Message<AccountsStrategy>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountsStrategy>(Message.FAIL).buildResponse();
		}
	}
}
