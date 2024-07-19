package com.wy.test.authorize.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstsStatus;
import com.wy.test.core.entity.Accounts;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.Message;

import dream.flying.flower.lang.StrHelper;

@RestController
@RequestMapping(value = { "/authz/credential" })
public class AuthorizeCredentialEndpoint extends AuthorizeBaseEndpoint {

	@GetMapping("/get/{appId}")
	public ResponseEntity<?> get(@PathVariable("appId") String appId, @CurrentUser UserInfo currentUser) {
		Apps app = getApp(appId);
		Accounts account = getAccounts(app, currentUser);
		if (account == null) {
			account = new Accounts();
			account.setId(account.generateId());

			account.setUserId(currentUser.getId());
			account.setUsername(currentUser.getUsername());
			account.setDisplayName(currentUser.getDisplayName());

			account.setAppId(appId);
			account.setAppName(app.getAppName());
			account.setInstId(currentUser.getInstId());
			account.setCreateType("manual");
			account.setStatus(ConstsStatus.ACTIVE);
		}
		return new Message<Accounts>(account).buildResponse();
	}

	@PostMapping("/update")
	public ResponseEntity<?> update(@RequestBody Accounts account, @CurrentUser UserInfo currentUser) {
		if (StrHelper.isNotEmpty(account.getRelatedPassword()) && StrHelper.isNotEmpty(account.getRelatedPassword())) {
			account.setInstId(currentUser.getInstId());
			account.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
			if (accountsService.get(account.getId()) == null) {
				if (accountsService.insert(account)) {
					return new Message<Accounts>().buildResponse();
				}
			} else {
				if (accountsService.update(account)) {
					return new Message<Accounts>().buildResponse();
				}
			}
		}

		return new Message<Accounts>(Message.FAIL).buildResponse();
	}
}