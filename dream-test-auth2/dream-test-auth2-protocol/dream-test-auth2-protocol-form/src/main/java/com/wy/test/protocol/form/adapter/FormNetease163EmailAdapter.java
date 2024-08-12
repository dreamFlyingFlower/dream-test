package com.wy.test.protocol.form.adapter;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.vo.AppFormDetailVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.framework.core.enums.BooleanEnum;

public class FormNetease163EmailAdapter extends AbstractAuthorizeAdapter {

	@Override
	public String generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/formbased_163email_sso_submint");
		AppFormDetailVO details = (AppFormDetailVO) app;
		modelAndView.addObject("username",
				account.getRelatedUsername().substring(account.getRelatedUsername().indexOf("@")));
		modelAndView.addObject("email", account.getRelatedUsername());
		modelAndView.addObject("password", account.getRelatedPassword());

		if (BooleanEnum.isTrue(details.getIsExtendAttr())) {
			modelAndView.addObject("extendAttr", details.getExtendAttr());
			modelAndView.addObject("isExtendAttr", true);
		} else {
			modelAndView.addObject("isExtendAttr", false);
		}

		modelAndView.addObject("action", details.getRedirectUri());
		modelAndView.addObject("usernameMapping", details.getUsernameMapping());
		modelAndView.addObject("passwordMapping", details.getPasswordMapping());
		return modelAndView;
	}
}