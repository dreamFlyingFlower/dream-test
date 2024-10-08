package com.wy.test.web.mgt.contorller;

import java.util.Arrays;
import java.util.List;

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

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstLogEntryType;
import com.wy.test.core.constant.ConstLogOperateType;
import com.wy.test.core.constant.ConstOperateResult;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AccountStrategyEntity;
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
	public ResponseEntity<?> fetch(AccountEntity accounts, @CurrentUser UserEntity currentUser) {
		log.debug("" + accounts);
		accounts.setInstId(currentUser.getInstId());
		return new ResultResponse<>(accountsService.list(accounts)).buildResponse();
	}

	@PostMapping(value = { "/query" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> query(AccountEntity account, @CurrentUser UserEntity currentUser) {
		log.debug("-query  :" + account);
		account.setInstId(currentUser.getInstId());
		List<AccountVO> accountVOs = accountsService.list(account);
		if (CollectionUtils.isNotEmpty(accountsService.list(account))) {
			return new ResultResponse<>(accountVOs).buildResponse();
		} else {
			return new ResultResponse<>(ResultResponse.SUCCESS).buildResponse();
		}
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AccountEntity account = accountsService.getById(id);
		account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(account.getRelatedPassword()));
		return new ResultResponse<AccountEntity>(account).buildResponse();
	}

	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> insert(@RequestBody AccountEntity account, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + account);
		account.setInstId(currentUser.getInstId());
		account.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
		if (accountsService.insert(account)) {
			systemLog.insert(ConstLogEntryType.ACCOUNT, account, ConstLogOperateType.CREATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new ResultResponse<AccountEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AccountEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AccountEntity account, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + account);
		account.setInstId(currentUser.getInstId());
		account.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
		if (accountsService.update(account)) {
			systemLog.insert(ConstLogEntryType.ACCOUNT, account, ConstLogOperateType.UPDATE, ConstOperateResult.SUCCESS,
					currentUser);
			return new ResultResponse<AccountEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AccountEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@PostMapping(value = { "/updateStatus" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> updateStatus(AccountEntity accounts, @CurrentUser UserEntity currentUser) {
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
			systemLog.insert(ConstLogEntryType.ACCOUNT, accounts, ConstLogOperateType.statusActon.get(accounts.getStatus()),
					ConstOperateResult.SUCCESS, currentUser);
			return new ResultResponse<AccountEntity>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<AccountEntity>(ResultResponse.FAIL).buildResponse();
		}
	}

	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete ids : {} ", ids);

		if (accountsService.removeByIds(Arrays.asList(ids.split(",")))) {
			systemLog.insert(ConstLogEntryType.ACCOUNT, ids, ConstLogOperateType.DELETE, ConstOperateResult.SUCCESS,
					currentUser);
			return new ResultResponse<>(ResultResponse.SUCCESS).buildResponse();
		} else {
			return new ResultResponse<>(ResultResponse.FAIL).buildResponse();
		}

	}

	@PostMapping(value = "/generate")
	public ResponseEntity<?> generate(AccountEntity account) {
		AccountStrategyEntity accountsStrategy = accountsStrategyService.getById(account.getStrategyId());
		UserEntity userInfo = userInfoService.getById(account.getUserId());
		return new ResultResponse<>(ResultResponse.SUCCESS, (Object) accountsService.generateAccount(userInfo, accountsStrategy))
				.buildResponse();
	}
}