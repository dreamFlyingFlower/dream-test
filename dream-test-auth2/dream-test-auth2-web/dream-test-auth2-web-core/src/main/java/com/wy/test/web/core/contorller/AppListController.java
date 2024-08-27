package com.wy.test.web.core.contorller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.enums.CredentialType;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.vo.UserApps;
import com.wy.test.core.vo.UserVO;
import com.wy.test.persistence.service.AccountService;
import com.wy.test.persistence.service.AppService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.generator.GeneratorStrategyContext;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
public class AppListController {

	@Autowired
	private UserService userService;

	@Autowired
	AccountService accountService;

	@Autowired
	AppService appService;

	/**
	 * gridList.
	 * 
	 * @param gridList 类型
	 * @return
	 */
	@GetMapping(value = { "/appList" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
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
		return new Message<List<UserApps>>(appList).buildResponse();
	}

	@GetMapping(value = { "/account/get" })
	@ResponseBody
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
		return new Message<AccountEntity>(account).buildResponse();

	}

	@PostMapping(value = { "/account/update" })
	@ResponseBody
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

		return new Message<AccountEntity>().buildResponse();
	}
}