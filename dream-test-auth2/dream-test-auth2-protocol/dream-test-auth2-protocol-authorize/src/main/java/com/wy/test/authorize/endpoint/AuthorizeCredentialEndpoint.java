package com.wy.test.authorize.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;

import dream.flying.flower.generator.GeneratorStrategyContext;
import dream.flying.flower.lang.StrHelper;

@RestController
@RequestMapping(value = { "/authz/credential" })
public class AuthorizeCredentialEndpoint extends AuthorizeBaseEndpoint {

	@GetMapping("/get/{appId}")
	public ResponseEntity<?> get(@PathVariable("appId") String appId, @CurrentUser UserEntity currentUser) {
		AppEntity app = getApp(appId);
		AccountEntity account = getAccounts(app, currentUser);
		if (account == null) {
			account = new AccountEntity();
			GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
			account.setId(generatorStrategyContext.generate());

			account.setUserId(currentUser.getId());
			account.setUsername(currentUser.getUsername());
			account.setDisplayName(currentUser.getDisplayName());

			account.setAppId(appId);
			account.setAppName(app.getAppName());
			account.setInstId(currentUser.getInstId());
			account.setCreateType("manual");
			account.setStatus(ConstStatus.ACTIVE);
		}
		return new Message<AccountEntity>(account).buildResponse();
	}

	@PostMapping("/update")
	public ResponseEntity<?> update(@RequestBody AccountEntity account, @CurrentUser UserEntity currentUser) {
		if (StrHelper.isNotEmpty(account.getRelatedPassword()) && StrHelper.isNotEmpty(account.getRelatedPassword())) {
			account.setInstId(currentUser.getInstId());
			account.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
			if (accountService.getById(account.getId()) == null) {
				if (accountService.insert(account)) {
					return new Message<AccountEntity>().buildResponse();
				}
			} else {
				if (accountService.update(account)) {
					return new Message<AccountEntity>().buildResponse();
				}
			}
		}

		return new Message<AccountEntity>(Message.FAIL).buildResponse();
	}
}