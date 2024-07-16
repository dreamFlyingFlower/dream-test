package com.wy.test.protocol.formbased.adapter;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.constants.ConstsBoolean;
import com.wy.test.entity.apps.AppsFormBasedDetails;

public class FormBasedNetease163EmailAdapter extends AbstractAuthorizeAdapter {

	@Override
	public String generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/formbased_163email_sso_submint");
		AppsFormBasedDetails details = (AppsFormBasedDetails) app;
		modelAndView.addObject("username",
				account.getRelatedUsername().substring(account.getRelatedUsername().indexOf("@")));
		modelAndView.addObject("email", account.getRelatedUsername());
		modelAndView.addObject("password", account.getRelatedPassword());

		if (ConstsBoolean.isTrue(details.getIsExtendAttr())) {
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
