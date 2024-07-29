package com.wy.test.web.contorller;

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
import com.wy.test.core.constants.ConstsEntryType;
import com.wy.test.core.constants.ConstsOperateAction;
import com.wy.test.core.constants.ConstsOperateResult;
import com.wy.test.core.crypto.password.PasswordReciprocal;
import com.wy.test.core.entity.Accounts;
import com.wy.test.core.entity.AccountsStrategy;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.persistence.service.AccountsService;
import com.wy.test.persistence.service.AccountsStrategyService;
import com.wy.test.persistence.service.AppsService;
import com.wy.test.persistence.service.HistorySystemLogsService;
import com.wy.test.persistence.service.UserInfoService;

@Controller
@RequestMapping(value = { "/accounts" })
public class AccountsController {

	final static Logger _logger = LoggerFactory.getLogger(AccountsController.class);

	@Autowired
	AccountsService accountsService;

	@Autowired
	AccountsStrategyService accountsStrategyService;

	@Autowired
	AppsService appsService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	HistorySystemLogsService systemLog;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute Accounts accounts, @CurrentUser UserInfo currentUser) {
		_logger.debug("" + accounts);
		accounts.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<Accounts>>(accountsService.fetchPageResults(accounts)).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute Accounts account, @CurrentUser UserInfo currentUser) {
		_logger.debug("-query  :" + account);
		account.setInstId(currentUser.getInstId());

		if (CollectionUtils.isNotEmpty(accountsService.query(account))) {
			return new Message<Accounts>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Accounts>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Accounts account = accountsService.get(id);
		account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(account.getRelatedPassword()));
		return new Message<Accounts>(account).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody Accounts account, @CurrentUser UserInfo currentUser) {
		_logger.debug("-Add  :" + account);
		account.setInstId(currentUser.getInstId());
		account.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
		if (accountsService.insert(account)) {
			systemLog.insert(ConstsEntryType.ACCOUNT, account, ConstsOperateAction.CREATE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Accounts>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Accounts>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody Accounts account, @CurrentUser UserInfo currentUser) {
		_logger.debug("-update  :" + account);
		account.setInstId(currentUser.getInstId());
		account.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
		if (accountsService.update(account)) {
			systemLog.insert(ConstsEntryType.ACCOUNT, account, ConstsOperateAction.UPDATE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Accounts>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Accounts>(Message.FAIL).buildResponse();
		}
	}

	@PostMapping(value = { "/updateStatus" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> updateStatus(@ModelAttribute Accounts accounts, @CurrentUser UserInfo currentUser) {
		_logger.debug("" + accounts);
		Accounts loadAccount = accountsService.get(accounts.getId());
		accounts.setInstId(currentUser.getInstId());
		accounts.setAppId(loadAccount.getAppId());
		accounts.setAppName(loadAccount.getAppName());
		accounts.setUserId(loadAccount.getUserId());
		accounts.setUsername(loadAccount.getUsername());
		accounts.setDisplayName(loadAccount.getDisplayName());
		accounts.setRelatedUsername(loadAccount.getRelatedUsername());
		if (accountsService.updateStatus(accounts)) {
			systemLog.insert(ConstsEntryType.ACCOUNT, accounts,
					ConstsOperateAction.statusActon.get(accounts.getStatus()), ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Accounts>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Accounts>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserInfo currentUser) {
		_logger.debug("-delete ids : {} ", ids);

		if (accountsService.deleteBatch(ids)) {
			systemLog.insert(ConstsEntryType.ACCOUNT, ids, ConstsOperateAction.DELETE, ConstsOperateResult.SUCCESS,
					currentUser);
			return new Message<Accounts>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<Accounts>(Message.FAIL).buildResponse();
		}

	}

	@ResponseBody
	@PostMapping(value = "/generate")
	public ResponseEntity<?> generate(@ModelAttribute Accounts account) {
		AccountsStrategy accountsStrategy = accountsStrategyService.get(account.getStrategyId());
		UserInfo userInfo = userInfoService.get(account.getUserId());
		return new Message<Object>(Message.SUCCESS,
				(Object) accountsService.generateAccount(userInfo, accountsStrategy)).buildResponse();
	}
}