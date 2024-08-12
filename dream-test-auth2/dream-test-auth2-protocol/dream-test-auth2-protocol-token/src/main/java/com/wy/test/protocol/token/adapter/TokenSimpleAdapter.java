package com.wy.test.protocol.token.adapter;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.vo.AppTokenDetailVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.helper.DateTimeHelper;

public class TokenSimpleAdapter extends AbstractAuthorizeAdapter {

	String token = "";

	@Override
	public Object generateInfo() {
		AppTokenDetailVO details = (AppTokenDetailVO) app;

		String tokenUsername = userInfo.getUsername();

		if (details.getUserPropertys() != null && !details.getUserPropertys().equals("")) {
			if (details.getUserPropertys().indexOf("userId") > -1) {
				tokenUsername = userInfo.getId();
			} else if (details.getUserPropertys().indexOf("username") > -1) {
				tokenUsername = userInfo.getUsername();
			} else if (details.getUserPropertys().indexOf("email") > -1) {
				tokenUsername = userInfo.getEmail();
			} else if (details.getUserPropertys().indexOf("windowsAccount") > -1) {
				tokenUsername = userInfo.getWindowsAccount();
			} else if (details.getUserPropertys().indexOf("employeeNumber") > -1) {
				tokenUsername = userInfo.getEmployeeNumber();
			} else if (details.getUserPropertys().indexOf("department") > -1) {
				tokenUsername = userInfo.getDepartmentId();
			} else if (details.getUserPropertys().indexOf("departmentId") > -1) {
				tokenUsername = userInfo.getDepartment();
			}
		}

		/*
		 * use UTC date time format
		 */
		token = tokenUsername + "@@" + DateTimeHelper.formatUtcDateTime();
		return token;
	}

	@Override
	public Object encrypt(Object data, String algorithmKey, String algorithm) {
		token = super.encrypt(token, algorithmKey, algorithm).toString();
		return token;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/tokenbased_sso_submint");
		AppTokenDetailVO details = (AppTokenDetailVO) app;
		modelAndView.addObject("action", details.getRedirectUri());

		modelAndView.addObject("token", token);

		return modelAndView;
	}

	@Override
	public String serialize() {
		return token;
	}
}