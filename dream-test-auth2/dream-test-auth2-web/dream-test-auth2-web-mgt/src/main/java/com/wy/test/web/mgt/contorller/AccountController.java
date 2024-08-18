package com.wy.test.web.mgt.contorller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstEntryType;
import com.wy.test.core.constant.ConstOperateAction;
import com.wy.test.core.constant.ConstOperateResult;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.vo.AccountVO;
import com.wy.test.persistence.service.AccountService;
import com.wy.test.persistence.service.AccountStrategyService;
import com.wy.test.persistence.service.AppService;
import com.wy.test.persistence.service.HistorySysLogService;
import com.wy.test.persistence.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/accounts" })
@Slf4j
public class AccountController {

	@Autowired
	AccountService accountsService;

	@Autowired
	AccountStrategyService accountsStrategyService;

	@Autowired
	AppService appsService;

	@Autowired
	UserService userInfoService;

	@Autowired
	HistorySysLogService systemLog;

	@PostMapping(value = { "/fetch" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> fetch(@ModelAttribute AccountEntity accounts, @CurrentUser UserEntity currentUser) {
		log.debug("" + accounts);
		accounts.setInstId(currentUser.getInstId());
		return new Message<>(accountsService.list(accounts)).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(@ModelAttribute AccountEntity account, @CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + account);
		account.setInstId(currentUser.getInstId());
		List<AccountVO> accountVOs = accountsService.list(account);
		if (CollectionUtils.isNotEmpty(accountsService.list(account))) {
			return new Message<>(accountVOs).buildResponse();
		} else {
			return new Message<>(Message.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AccountEntity account = accountsService.getById(id);
		account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(account.getRelatedPassword()));
		return new Message<AccountEntity>(account).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AccountEntity account, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + account);
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
		log.debug("-update  :" + account);
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
		log.debug("" + accounts);
		AccountEntity loadAccount = accountsService.getById(accounts.getId());
		accounts.setInstId(currentUser.getInstId());
		accounts.setAppId(loadAccount.getAppId());
		accounts.setAppName(loadAccount.getAppName());
		accounts.setUserId(loadAccount.getUserId());
		accounts.setUsername(loadAccount.getUsername());
		accounts.setDisplayName(loadAccount.getDisplayName());
		accounts.setRelatedUsername(loadAccount.getRelatedUsername());
		if (accountsService.updateStatus(accounts)) {
			systemLog.insert(ConstEntryType.ACCOUNT, accounts, ConstOperateAction.statusActon.get(accounts.getStatus()),
					ConstOperateResult.SUCCESS, currentUser);
			return new Message<AccountEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AccountEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete ids : {} ", ids);

		if (accountsService.removeByIds(Arrays.asList(ids.split(",")))) {
			systemLog.insert(ConstEntryType.ACCOUNT, ids, ConstOperateAction.DELETE, ConstOperateResult.SUCCESS,
					currentUser);
			return new Message<>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<>(Message.FAIL).buildResponse();
		}

	}

	@ResponseBody
	@PostMapping(value = "/generate")
	public ResponseEntity<?> generate(@ModelAttribute AccountEntity account) {
		AccountStrategyEntity accountsStrategy = accountsStrategyService.getById(account.getStrategyId());
		UserEntity userInfo = userInfoService.getById(account.getUserId());
		return new Message<>(Message.SUCCESS, (Object) accountsService.generateAccount(userInfo, accountsStrategy))
				.buildResponse();
	}
}