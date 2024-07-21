package com.wy.test.authorize.endpoint;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.common.crypto.password.PasswordReciprocal;
import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.core.entity.Accounts;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.AccountsService;
import com.wy.test.persistence.service.AppsService;

public class AuthorizeBaseEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(AuthorizeBaseEndpoint.class);

	@Autowired
	protected ApplicationConfig applicationConfig;

	@Autowired
	protected AppsService appsService;

	@Autowired
	protected AccountsService accountsService;

	protected Apps getApp(String id) {
		Apps app = (Apps) WebContext.getAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP);
		if (StringUtils.isBlank(id)) {
			_logger.error("parameter for app id " + id + "  is null.");
		} else {
			// session中为空或者id不一致重新加载
			if (app == null || !app.getId().equalsIgnoreCase(id)) {
				app = appsService.get(id, true);
			}
			WebContext.setAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP, app);
		}
		if (app == null) {
			_logger.error("Applications id " + id + "  is not exist.");
		}
		return app;
	}

	protected Accounts getAccounts(Apps app, UserInfo userInfo) {
		Apps loadApp = getApp(app.getId());

		Accounts account = new Accounts(userInfo.getId(), loadApp.getId());
		account.setUsername(userInfo.getUsername());
		account.setAppName(app.getAppName());

		if (loadApp.getCredential().equalsIgnoreCase(Apps.CREDENTIALS.USER_DEFINED)) {
			List<Accounts> query = accountsService.query(new Accounts(userInfo.getId(), loadApp.getId()));
			account = CollectionUtils.isNotEmpty(query) ? query.get(0) : null;
			if (account != null) {
				account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(account.getRelatedPassword()));
			}

		} else if (loadApp.getCredential().equalsIgnoreCase(Apps.CREDENTIALS.SHARED)) {
			account.setRelatedUsername(loadApp.getSharedUsername());
			account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(loadApp.getSharedPassword()));
		} else if (loadApp.getCredential().equalsIgnoreCase(Apps.CREDENTIALS.SYSTEM)) {
			account.setUsername(AbstractAuthorizeAdapter.getValueByUserAttr(userInfo, loadApp.getSystemUserAttr()));
			// decoder database stored encode password
			account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(userInfo.getDecipherable()));
		} else if (loadApp.getCredential().equalsIgnoreCase(Apps.CREDENTIALS.NONE)) {
			account.setUsername(userInfo.getUsername());
			account.setRelatedPassword(userInfo.getUsername());

		}
		return account;
	}

	public ModelAndView initCredentialView(String appId, String redirect_uri) {
		String initCredentialURL =
				"" + applicationConfig.getFrontendUri() + "/#/authz/credential?appId=%s&redirect_uri=%s";

		initCredentialURL = String.format(initCredentialURL, appId, redirect_uri);
		_logger.debug("redirect to {}.", initCredentialURL);
		ModelAndView modelAndView = new ModelAndView("redirect");
		modelAndView.addObject("redirect_uri", initCredentialURL);
		return modelAndView;
	}

}
