package com.wy.test.authz.token.endpoint.adapter;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.common.util.DateUtils;
import com.wy.test.core.entity.apps.AppsTokenBasedDetails;

public class TokenBasedSimpleAdapter extends AbstractAuthorizeAdapter {

	final static Logger _logger = LoggerFactory.getLogger(TokenBasedSimpleAdapter.class);

	String token = "";

	@Override
	public Object generateInfo() {
		AppsTokenBasedDetails details = (AppsTokenBasedDetails) app;

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
		Date currentDate = new Date();
		_logger.debug("UTC Local current date : " + DateUtils.toUtcLocal(currentDate));
		_logger.debug("UTC  current Date : " + DateUtils.toUtc(currentDate));

		token = tokenUsername + "@@" + DateUtils.toUtc(currentDate);
		_logger.debug("Token : {}", token);

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
		AppsTokenBasedDetails details = (AppsTokenBasedDetails) app;
		modelAndView.addObject("action", details.getRedirectUri());

		modelAndView.addObject("token", token);

		return modelAndView;
	}

	@Override
	public String serialize() {
		return token;
	}
}
