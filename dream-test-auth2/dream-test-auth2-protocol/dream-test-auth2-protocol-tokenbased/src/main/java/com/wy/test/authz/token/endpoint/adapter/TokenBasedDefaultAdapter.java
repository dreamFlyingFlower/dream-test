package com.wy.test.authz.token.endpoint.adapter;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.common.util.JsonUtils;
import com.wy.test.common.util.StringGenerator;
import com.wy.test.core.entity.apps.AppsTokenBasedDetails;
import com.wy.test.core.web.WebConstants;

import dream.flying.flower.helper.DateTimeHelper;

public class TokenBasedDefaultAdapter extends AbstractAuthorizeAdapter {

	final static Logger _logger = LoggerFactory.getLogger(TokenBasedDefaultAdapter.class);

	String token = "";

	@Override
	public Object generateInfo() {
		AppsTokenBasedDetails details = (AppsTokenBasedDetails) app;
		HashMap<String, String> beanMap = new HashMap<String, String>();

		beanMap.put("randomId", (new StringGenerator()).uuidGenerate());
		if (details.getUserPropertys() != null && !details.getUserPropertys().equals("")) {

			if (details.getUserPropertys().indexOf("userId") > -1) {
				beanMap.put("userId", userInfo.getId());
			}

			if (details.getUserPropertys().indexOf("username") > -1) {
				beanMap.put("username", userInfo.getUsername());
			}

			if (details.getUserPropertys().indexOf("email") > -1) {
				beanMap.put("email", userInfo.getEmail());
			}

			if (details.getUserPropertys().indexOf("windowsAccount") > -1) {
				beanMap.put("windowsAccount", userInfo.getWindowsAccount());
			}

			if (details.getUserPropertys().indexOf("employeeNumber") > -1) {
				beanMap.put("employeeNumber", userInfo.getEmployeeNumber());
			}

			if (details.getUserPropertys().indexOf("department") > -1) {
				beanMap.put("department", userInfo.getDepartment());
			}

			if (details.getUserPropertys().indexOf("departmentId") > -1) {
				beanMap.put("departmentId", userInfo.getDepartmentId());
			}
		}

		beanMap.put("displayName", userInfo.getDisplayName());
		beanMap.put(WebConstants.ONLINE_TICKET_NAME, principal.getSession().getFormattedId());

		/*
		 * use UTC date time format current date plus expires minute
		 */
		LocalDateTime localDateTime = LocalDateTime.now();
		beanMap.put("at", DateTimeHelper.formatUtcDateTime(localDateTime));
		beanMap.put("expires", DateTimeHelper.formatUtcDateTime(localDateTime.plusSeconds(details.getExpires())));

		token = JsonUtils.toString(beanMap);
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
