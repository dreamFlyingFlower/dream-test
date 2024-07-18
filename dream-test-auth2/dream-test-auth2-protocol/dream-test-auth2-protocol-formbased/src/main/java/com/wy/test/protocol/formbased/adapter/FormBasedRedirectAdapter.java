package com.wy.test.protocol.formbased.adapter;

import java.time.Instant;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.entity.apps.AppsFormBasedDetails;
import com.wy.test.core.web.WebContext;
import com.wy.test.crypto.DigestUtils;

public class FormBasedRedirectAdapter extends AbstractAuthorizeAdapter {

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/formbased_redirect_submint");
		AppsFormBasedDetails details = (AppsFormBasedDetails) app;

		String password = account.getRelatedPassword();
		if (null == details.getPasswordAlgorithm() || details.getPasswordAlgorithm().equals("")) {
		} else if (details.getPasswordAlgorithm().indexOf("HEX") > -1) {
			password = DigestUtils.digestHex(account.getRelatedPassword(),
					details.getPasswordAlgorithm().substring(0, details.getPasswordAlgorithm().indexOf("HEX")));
		} else {
			password = DigestUtils.digestBase64(account.getRelatedPassword(), details.getPasswordAlgorithm());
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

		if (WebContext.getAttribute("formbased_redirect_submint") == null) {
			modelAndView.setViewName("authorize/formbased_redirect_submint");
			WebContext.setAttribute("formbased_redirect_submint", "formbased_redirect_submint");
		} else {
			modelAndView.setViewName("authorize/formbased_redirect_post_submint");
			if (details.getAuthorizeView() != null && !details.getAuthorizeView().equals("")) {
				modelAndView.setViewName("authorize/" + details.getAuthorizeView());
			}
			WebContext.removeAttribute("formbased_redirect_submint");
		}

		if (ConstsBoolean.isTrue(details.getIsExtendAttr())) {
			modelAndView.addObject("extendAttr", details.getExtendAttr());
			modelAndView.addObject("isExtendAttr", true);
		} else {
			modelAndView.addObject("isExtendAttr", false);
		}

		return modelAndView;
	}

}
