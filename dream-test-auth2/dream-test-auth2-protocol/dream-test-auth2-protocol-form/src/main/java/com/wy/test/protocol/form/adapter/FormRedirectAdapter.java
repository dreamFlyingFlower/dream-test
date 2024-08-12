package com.wy.test.protocol.form.adapter;

import java.time.Instant;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.vo.AppFormDetailVO;
import com.wy.test.core.web.WebContext;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.digest.enums.MessageDigestType;
import dream.flying.flower.framework.core.enums.BooleanEnum;

public class FormRedirectAdapter extends AbstractAuthorizeAdapter {

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/formbased_redirect_submint");
		AppFormDetailVO details = (AppFormDetailVO) app;

		String password = account.getRelatedPassword();
		if (null == details.getPasswordAlgorithm() || details.getPasswordAlgorithm().equals("")) {
		} else if (details.getPasswordAlgorithm().indexOf("HEX") > -1) {
			password = DigestHelper.digestHex(
					MessageDigestType.getType(
							details.getPasswordAlgorithm().substring(0, details.getPasswordAlgorithm().indexOf("HEX"))),
					account.getRelatedPassword());
		} else {
			password = DigestHelper.digestBase64(MessageDigestType.getType(details.getPasswordAlgorithm()),
					account.getRelatedPassword());
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

		if (BooleanEnum.isTrue(details.getIsExtendAttr())) {
			modelAndView.addObject("extendAttr", details.getExtendAttr());
			modelAndView.addObject("isExtendAttr", true);
		} else {
			modelAndView.addObject("isExtendAttr", false);
		}

		return modelAndView;
	}
}