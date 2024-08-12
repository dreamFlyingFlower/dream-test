package com.wy.test.protocol.authorize.endpoint;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.enums.CredentialType;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.AccountService;
import com.wy.test.persistence.service.AppService;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizeBaseEndpoint {

	@Autowired
	protected DreamAuthServerProperties dreamServerProperties;

	@Autowired
	protected AppService appService;

	@Autowired
	protected AccountService accountService;

	protected AppVO getApp(String id) {
		AppVO app = (AppVO) WebContext.getAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP);
		if (StringUtils.isBlank(id)) {
			log.error("parameter for app id " + id + "  is null.");
		} else {
			// session中为空或者id不一致重新加载
			if (app == null || !app.getId().equalsIgnoreCase(id)) {
				app = appService.get(id, true);
			}
			WebContext.setAttribute(WebConstants.AUTHORIZE_SIGN_ON_APP, app);
		}
		if (app == null) {
			log.error("Applications id " + id + "  is not exist.");
		}
		return app;
	}

	protected AccountEntity getAccounts(AppVO app, UserVO userInfo) {
		AppVO loadApp = getApp(app.getId());

		AccountEntity account = new AccountEntity(userInfo.getId(), loadApp.getId());
		account.setUsername(userInfo.getUsername());
		account.setAppName(app.getAppName());

		if (loadApp.getCredential().equalsIgnoreCase(CredentialType.USER_DEFINED.name())) {
			List<AccountEntity> query = accountService.list(new LambdaQueryWrapper<AccountEntity>()
					.eq(AccountEntity::getUserId, userInfo.getId()).eq(AccountEntity::getAppId, loadApp.getId()));
			account = CollectionUtils.isNotEmpty(query) ? query.get(0) : null;
			if (account != null) {
				account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(account.getRelatedPassword()));
			}
		} else if (loadApp.getCredential().equalsIgnoreCase(CredentialType.SHARED.name())) {
			account.setRelatedUsername(loadApp.getSharedUsername());
			account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(loadApp.getSharedPassword()));
		} else if (loadApp.getCredential().equalsIgnoreCase(CredentialType.SYSTEM.name())) {
			account.setUsername(AbstractAuthorizeAdapter.getValueByUserAttr(userInfo, loadApp.getSysUserAttr()));
			// decoder database stored encode password
			account.setRelatedPassword(PasswordReciprocal.getInstance().decoder(userInfo.getDecipherable()));
		} else if (loadApp.getCredential().equalsIgnoreCase(CredentialType.NONE.name())) {
			account.setUsername(userInfo.getUsername());
			account.setRelatedPassword(userInfo.getUsername());
		}
		return account;
	}

	public ModelAndView initCredentialView(String appId, String redirect_uri) {
		String initCredentialURL =
				"" + dreamServerProperties.getFrontendUri() + "/#/authz/credential?appId=%s&redirect_uri=%s";

		initCredentialURL = String.format(initCredentialURL, appId, redirect_uri);
		log.debug("redirect to {}.", initCredentialURL);
		ModelAndView modelAndView = new ModelAndView("redirect");
		modelAndView.addObject("redirect_uri", initCredentialURL);
		return modelAndView;
	}
}