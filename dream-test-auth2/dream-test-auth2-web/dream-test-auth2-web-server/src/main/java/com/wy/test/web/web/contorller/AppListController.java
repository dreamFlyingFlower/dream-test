package com.wy.test.web.web.contorller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.constants.ConstsStatus;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.Accounts;
import com.wy.test.entity.Message;
import com.wy.test.entity.UserInfo;
import com.wy.test.entity.apps.Apps;
import com.wy.test.entity.apps.UserApps;
import com.wy.test.persistence.service.AccountsService;
import com.wy.test.persistence.service.AppsService;
import com.wy.test.persistence.service.UserInfoService;

/**
 * AppListController.
 * 
 */
@Controller
public class AppListController {

	static final Logger _logger = LoggerFactory.getLogger(AppListController.class);

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	AccountsService accountsService;

	@Autowired
	AppsService appsService;

	/**
	 * gridList.
	 * 
	 * @param gridList 类型
	 * @return
	 */
	@RequestMapping(value = { "/appList" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> appList(@RequestParam(value = "gridList", required = false) String gridList,
			@CurrentUser UserInfo currentUser) {
		userInfoService.updateGridList(gridList, currentUser);
		UserApps userApps = new UserApps();
		userApps.setUsername(currentUser.getUsername());
		userApps.setInstId(currentUser.getInstId());
		List<UserApps> appList = appsService.queryMyApps(userApps);
		for (UserApps app : appList) {
			app.transIconBase64();
		}
		// AuthorizationUtils.setAuthentication(null);
		return new Message<List<UserApps>>(appList).buildResponse();
	}

	@RequestMapping(value = { "/account/get" })
	@ResponseBody
	public ResponseEntity<?> getAccount(@RequestParam("credential") String credential,
			@RequestParam("appId") String appId, @CurrentUser UserInfo currentUser) {
		Accounts account = null;

		if (credential.equalsIgnoreCase(Apps.CREDENTIALS.USER_DEFINED)) {
			List<Accounts> query = accountsService.query(new Accounts(currentUser.getId(), appId));

			account = CollectionUtils.isNotEmpty(query) ? query.get(0) : new Accounts();
			account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(account.getRelatedPassword()));
		} else {
			account = new Accounts();
			account.setAppId(appId);
			account.setUserId(currentUser.getId());
			account.setUsername(currentUser.getUsername());
			account.setDisplayName(currentUser.getDisplayName());
		}
		return new Message<Accounts>(account).buildResponse();

	}

	@RequestMapping(value = { "/account/update" })
	@ResponseBody
	public ResponseEntity<?> updateAccount(@RequestParam("credential") String credential,
			@ModelAttribute Accounts account, @CurrentUser UserInfo currentUser) {
		Accounts appUsers = new Accounts();
		if (credential.equalsIgnoreCase(Apps.CREDENTIALS.USER_DEFINED)) {
			List<Accounts> query = accountsService.query(new Accounts(currentUser.getId(), account.getAppId()));
			appUsers = CollectionUtils.isNotEmpty(query) ? query.get(0) : null;
			if (appUsers == null) {
				appUsers = new Accounts();
				appUsers.setId(appUsers.generateId());
				appUsers.setUserId(currentUser.getId());
				appUsers.setUsername(currentUser.getUsername());
				appUsers.setDisplayName(currentUser.getDisplayName());

				appUsers.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
				appUsers.setInstId(currentUser.getInstId());
				appUsers.setStatus(ConstsStatus.ACTIVE);
				accountsService.insert(appUsers);
			} else {
				appUsers.setRelatedUsername(account.getRelatedUsername());
				appUsers.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
				accountsService.update(appUsers);
			}
		}

		return new Message<Accounts>().buildResponse();
	}
}
