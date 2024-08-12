package com.wy.test.protocol.form.adapter;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.vo.AppFormDetailVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.digest.enums.MessageDigestType;
import dream.flying.flower.framework.core.enums.BooleanEnum;

public class FormDefaultAdapter extends AbstractAuthorizeAdapter {

	static String _HEX = "_HEX";

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/formbased_sso_submint");
		AppFormDetailVO details = (AppFormDetailVO) app;

		String password = account.getRelatedPassword();
		String passwordAlgorithm = details.getPasswordAlgorithm();

		if (StringUtils.isBlank(passwordAlgorithm) || passwordAlgorithm.equalsIgnoreCase("NONE")) {
			// do nothing
		} else if (passwordAlgorithm.indexOf(_HEX) > -1) {
			passwordAlgorithm = passwordAlgorithm.substring(0, passwordAlgorithm.indexOf(_HEX));
			password =
					DigestHelper.digestHex(MessageDigestType.getType(passwordAlgorithm), account.getRelatedPassword());
		} else {
			password = DigestHelper.digestBase64(MessageDigestType.getType(passwordAlgorithm),
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

		if (BooleanEnum.isTrue(details.getIsExtendAttr())) {
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