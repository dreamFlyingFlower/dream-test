package com.wy.test.protocol.formbased.adapter;

import java.util.Date;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.core.entity.apps.AppsFormBasedDetails;

import dream.flying.flower.digest.DigestHelper;

public class FormBasedNeteaseNoteYoudaoAdapter extends AbstractAuthorizeAdapter {

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/formbased_wy_youdao_sso_submint");
		AppsFormBasedDetails details = (AppsFormBasedDetails) app;
		modelAndView.addObject("username", account.getRelatedUsername());
		modelAndView.addObject("password", DigestHelper.md5Hex(account.getRelatedPassword()));
		modelAndView.addObject("currentTime", (new Date()).getTime());
		modelAndView.addObject("app", details);
		return modelAndView;
	}
}