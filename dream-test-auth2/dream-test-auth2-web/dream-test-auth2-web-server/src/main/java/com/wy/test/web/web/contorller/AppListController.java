package com.wy.test.web.web.contorller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AppEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.vo.UserApps;
import com.wy.test.persistence.service.AccountsService;
import com.wy.test.persistence.service.AppsService;
import com.wy.test.persistence.service.UserInfoService;

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
	@GetMapping(value = { "/appList" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<?> appList(@RequestParam(value = "gridList", required = false) String gridList,
			@CurrentUser UserEntity currentUser) {
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

	@GetMapping(value = { "/account/get" })
	@ResponseBody
	public ResponseEntity<?> getAccount(@RequestParam("credential") String credential,
			@RequestParam("appId") String appId, @CurrentUser UserEntity currentUser) {
		AccountEntity account = null;

		if (credential.equalsIgnoreCase(AppEntity.CREDENTIALS.USER_DEFINED)) {
			List<AccountEntity> query = accountsService.query(new AccountEntity(currentUser.getId(), appId));

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
			@ModelAttribute AccountEntity account, @CurrentUser UserEntity currentUser) {
		AccountEntity appUsers = new AccountEntity();
		if (credential.equalsIgnoreCase(AppEntity.CREDENTIALS.USER_DEFINED)) {
			List<AccountEntity> query = accountsService.query(new AccountEntity(currentUser.getId(), account.getAppId()));
			appUsers = CollectionUtils.isNotEmpty(query) ? query.get(0) : null;
			if (appUsers == null) {
				appUsers = new AccountEntity();
				appUsers.setId(appUsers.generateId());
				appUsers.setUserId(currentUser.getId());
				appUsers.setUsername(currentUser.getUsername());
				appUsers.setDisplayName(currentUser.getDisplayName());

				appUsers.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
				appUsers.setInstId(currentUser.getInstId());
				appUsers.setStatus(ConstStatus.ACTIVE);
				accountsService.insert(appUsers);
			} else {
				appUsers.setRelatedUsername(account.getRelatedUsername());
				appUsers.setRelatedPassword(PasswordReciprocal.getInstance().encode(account.getRelatedPassword()));
				accountsService.update(appUsers);
			}
		}

		return new Message<AccountEntity>().buildResponse();
	}
}
