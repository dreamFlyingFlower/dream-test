package com.wy.test.protocol.formbased.adapter;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.entity.apps.AppsFormBasedDetails;
import com.wy.test.crypto.DigestUtils;

public class FormBasedDefaultAdapter extends AbstractAuthorizeAdapter {

	static String _HEX = "_HEX";

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/formbased_sso_submint");
		AppsFormBasedDetails details = (AppsFormBasedDetails) app;

		String password = account.getRelatedPassword();
		String passwordAlgorithm = details.getPasswordAlgorithm();

		if (StringUtils.isBlank(passwordAlgorithm) || passwordAlgorithm.equalsIgnoreCase("NONE")) {
			// do nothing
		} else if (passwordAlgorithm.indexOf(_HEX) > -1) {
			passwordAlgorithm = passwordAlgorithm.substring(0, passwordAlgorithm.indexOf(_HEX));
			password = DigestUtils.digestHex(account.getRelatedPassword(), passwordAlgorithm);
		} else {
			password = DigestUtils.digestBase64(account.getRelatedPassword(), passwordAlgorithm);
		}

		modelAndView.addObject("id", details.getId());
		modelAndView.addObject("action", details.getRedirectUri());
		modelAndView.addObject("redirectUri", details.getRedirectUri());
		modelAndView.addObject("loginUrl", details.getLoginUrl());
		modelAndView.addObject("usernameMapping", details.getUsernameMapping());
		modelAndView.addObject("passwordMapping", details.getPasswordMapping());
		modelAndView.addObject("username", account.getRelatedUsername());
		modelAndView.addObject("password", password);
		modelAndView.addObject("timestamp", "" + Instant.now().getEpochSecond());

		if (ConstsBoolean.isTrue(details.getIsExtendAttr())) {
			modelAndView.addObject("extendAttr", details.getExtendAttr());
			modelAndView.addObject("isExtendAttr", true);
		} else {
			modelAndView.addObject("isExtendAttr", false);
		}

		if (StringUtils.isNotBlank(details.getAuthorizeView())) {
			modelAndView.setViewName("authorize/" + details.getAuthorizeView());
		}

		return modelAndView;
	}

}
