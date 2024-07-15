package com.wy.test.authorize.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.constants.ConstsStatus;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.Accounts;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.entity.apps.Apps;
import com.wy.test.util.StringUtils;

@Controller
@RequestMapping(value = { "/authz/credential" })
public class AuthorizeCredentialEndpoint extends AuthorizeBaseEndpoint {

	@RequestMapping("/get/{appId}")
	@ResponseBody
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

	@RequestMapping("/update")
	public ResponseEntity<?> update(@RequestBody Accounts account, @CurrentUser UserInfo currentUser) {
		if (StringUtils.isNotEmpty(account.getRelatedPassword())
				&& StringUtils.isNotEmpty(account.getRelatedPassword())) {
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
