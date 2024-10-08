package com.wy.test.web.core.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.enums.CredentialType;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.vo.UserApps;
import com.wy.test.core.vo.UserVO;
import com.wy.test.persistence.service.AccountService;
import com.wy.test.persistence.service.AppService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.generator.GeneratorStrategyContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "1-7 APP应用API")
@RestController
@AllArgsConstructor
public class AppListController {

	final UserService userService;

	final AccountService accountService;

	final AppService appService;

	@Operation(summary = "获得用户APP列表", description = "获得用户APP列表", method = "GET")
	@GetMapping(value = { "/appList" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> appList(@RequestParam(value = "gridList", required = false) String gridList,
			@CurrentUser UserVO currentUser) {
		userService.updateGridList(gridList, currentUser);
		UserApps userApps = new UserApps();
		userApps.setUsername(currentUser.getUsername());
		userApps.setInstId(currentUser.getInstId());
		List<UserApps> appList = appService.queryMyApps(userApps);
		for (UserApps app : appList) {
			app.transIconBase64();
		}
		// AuthorizationUtils.setAuthentication(null);
		return new ResultResponse<List<UserApps>>(appList).buildResponse();
	}

	@Operation(summary = "获得用户帐号信息", description = "获得用户帐号信息", method = "GET")
	@GetMapping(value = { "/account/get" })
	public ResponseEntity<?> getAccount(@RequestParam("credential") String credential,
			@RequestParam("appId") String appId, @CurrentUser UserEntity currentUser) {
		AccountEntity account = null;

		if (credential.equalsIgnoreCase(CredentialType.USER_DEFINED.name())) {
			List<AccountEntity> query = accountService
					.list(new LambdaQueryWrapper<AccountEntity>().eq(AccountEntity::getUserId, currentUser.getId())
							.eq(AccountEntity::getAppId, appId));

			account = CollectionUtils.isNotEmpty(query) ? query.get(0) : new AccountEntity();
			account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(account.getRelatedPassword()));
		} else {
			account = new AccountEntity();
			account.setAppId(appId);
			account.setUserId(currentUser.getId());
			account.setUsername(currentUser.getUsername());
			account.setDisplayName(currentUser.getDisplayName());
		}
		return new ResultResponse<AccountEntity>(account).buildResponse();

	}

	@Operation(summary = "更新用户帐号信息", description = "更新用户帐号信息", method = "POST")
	@PostMapping(value = { "/account/update" })
	public ResponseEntity<?> updateAccount(@RequestParam("credential") String credential,
			@RequestBody AccountEntity account, @CurrentUser UserEntity currentUser) {
		AccountEntity appUsers = new AccountEntity();
		if (credential.equalsIgnoreCase(CredentialType.USER_DEFINED.name())) {
			List<AccountEntity> query = accountService
					.list(new LambdaQueryWrapper<AccountEntity>().eq(AccountEntity::getUserId, currentUser.getId())
							.eq(AccountEntity::getAppId, account.getAppId()));
			appUsers = CollectionUtils.isNotEmpty(query) ? query.get(0) : null;
			if (appUsers == null) {
				appUsers = new AccountEntity();
				GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
				appUsers.setId(generatorStrategyContext.generate());
				appUsers.setUserId(currentUser.getId());
				appUsers.setUsername(currentUser.getUsername());
				appUsers.setDisplayName(currentUser.getDisplayName());

				appUsers.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
				appUsers.setInstId(currentUser.getInstId());
				appUsers.setStatus(ConstStatus.ACTIVE);
				accountService.insert(appUsers);
			} else {
				appUsers.setRelatedUsername(account.getRelatedUsername());
				appUsers.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
				accountService.update(appUsers);
			}
		}

		return new ResultResponse<AccountEntity>().buildResponse();
	}
}