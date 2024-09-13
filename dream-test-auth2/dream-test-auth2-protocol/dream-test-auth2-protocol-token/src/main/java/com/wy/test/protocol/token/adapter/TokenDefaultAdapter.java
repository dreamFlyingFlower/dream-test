package com.wy.test.protocol.token.adapter;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.vo.AppTokenDetailVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.generator.StringGenerator;
import dream.flying.flower.helper.DateTimeHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenDefaultAdapter extends AbstractAuthorizeAdapter {

	String token = "";

	@Override
	public Object generateInfo() {
		AppTokenDetailVO details = (AppTokenDetailVO) app;
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
		beanMap.put(ConstAuthWeb.ONLINE_TICKET_NAME, principal.getSession().getFormattedId());

		/*
		 * use UTC date time format current date plus expires minute
		 */
		LocalDateTime localDateTime = LocalDateTime.now();
		beanMap.put("at", DateTimeHelper.formatUtcDateTime(localDateTime));
		beanMap.put("expires", DateTimeHelper.formatUtcDateTime(localDateTime.plusSeconds(details.getExpires())));

		token = JsonHelpers.toString(beanMap);
		log.debug("Token : {}", token);

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