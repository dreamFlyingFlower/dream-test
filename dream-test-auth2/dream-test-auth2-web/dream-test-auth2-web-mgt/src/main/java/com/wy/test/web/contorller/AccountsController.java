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
import com.wy.test.core.constants.ConstEntryType;
import com.wy.test.core.constants.ConstOperateAction;
import com.wy.test.core.constants.ConstOperateResult;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
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
	public ResponseEntity<?> fetch(@ModelAttribute AccountEntity accounts, @CurrentUser UserEntity currentUser) {
		_logger.debug("" + accounts);
		accounts.setInstId(currentUser.getInstId());
		return new Message<JpaPageResults<AccountEntity>>(accountsService.fetchPageResults(accounts)).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute AccountEntity account, @CurrentUser UserEntity currentUser) {
		_logger.debug("-query  :" + account);
		account.setInstId(currentUser.getInstId());

		if (CollectionUtils.isNotEmpty(accountsService.query(account))) {
			return new Message<AccountEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountEntity>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AccountEntity account = accountsService.get(id);
		account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(account.getRelatedPassword()));
		return new Message<AccountEntity>(account).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AccountEntity account, @CurrentUser UserEntity currentUser) {
		_logger.debug("-Add  :" + account);
		account.setInstId(currentUser.getInstId());
		account.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
		if (accountsService.insert(account)) {
			systemLog.insert(ConstEntryType.ACCOUNT, account, ConstOperateAction.CREATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<AccountEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AccountEntity account, @CurrentUser UserEntity currentUser) {
		_logger.debug("-update  :" + account);
		account.setInstId(currentUser.getInstId());
		account.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
		if (accountsService.update(account)) {
			systemLog.insert(ConstEntryType.ACCOUNT, account, ConstOperateAction.UPDATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<AccountEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountEntity>(Message.FAIL).buildResponse();
		}
	}

	@PostMapping(value = { "/updateStatus" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> updateStatus(@ModelAttribute AccountEntity accounts, @CurrentUser UserEntity currentUser) {
		_logger.debug("" + accounts);
		AccountEntity loadAccount = accountsService.get(accounts.getId());
		accounts.setInstId(currentUser.getInstId());
		accounts.setAppId(loadAccount.getAppId());
		accounts.setAppName(loadAccount.getAppName());
		accounts.setUserId(loadAccount.getUserId());
		accounts.setUsername(loadAccount.getUsername());
		accounts.setDisplayName(loadAccount.getDisplayName());
		accounts.setRelatedUsername(loadAccount.getRelatedUsername());
		if (accountsService.updateStatus(accounts)) {
			systemLog.insert(ConstEntryType.ACCOUNT, accounts,
					ConstOperateAction.statusActon.get(accounts.getStatus()), ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<AccountEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		_logger.debug("-delete ids : {} ", ids);

		if (accountsService.deleteBatch(ids)) {
			systemLog.insert(ConstEntryType.ACCOUNT, ids, ConstOperateAction.DELETE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<AccountEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountEntity>(Message.FAIL).buildResponse();
		}

	}

	@ResponseBody
	@PostMapping(value = "/generate")
	public ResponseEntity<?> generate(@ModelAttribute AccountEntity account) {
		AccountStrategyEntity accountsStrategy = accountsStrategyService.get(account.getStrategyId());
		UserEntity userInfo = userInfoService.get(account.getUserId());
		return new Message<Object>(Message.SUCCESS,
				(Object) accountsService.generateAccount(userInfo, accountsStrategy)).buildResponse();
	}
}