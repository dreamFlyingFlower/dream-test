package com.wy.test.core.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.filter.GenericFilterBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebXssRequestFilter extends GenericFilterBean {

	final static ConcurrentHashMap<String, String> skipUrlMap = new ConcurrentHashMap<String, String>();

	final static ConcurrentHashMap<String, String> skipParameterName = new ConcurrentHashMap<String, String>();

	static {
		// add or update
		skipUrlMap.put("/notices/add", "/notices/add");
		skipUrlMap.put("/notices/update", "/notices/update");
		skipUrlMap.put("/institutions/update", "/institutions/update");
		skipUrlMap.put("/localization/update", "/localization/update");
		skipUrlMap.put("/apps/updateExtendAttr", "/apps/updateExtendAttr");

		// authz
		skipUrlMap.put("/authz/cas", "/authz/cas");
		skipUrlMap.put("/authz/cas/", "/authz/cas/");
		skipUrlMap.put("/authz/cas/login", "/authz/cas/login");
		skipUrlMap.put("/authz/oauth/v20/authorize", "/authz/oauth/v20/authorize");
		// TENCENT_IOA
		skipUrlMap.put("/oauth2/authorize", "/oauth2/authorize");

		skipParameterName.put("relatedPassword", "relatedPassword");
		skipParameterName.put("oldPassword", "oldPassword");
		skipParameterName.put("password", "password");
		skipParameterName.put("confirmpassword", "confirmpassword");
		skipParameterName.put("credentials", "credentials");
		skipParameterName.put("clientSecret", "clientSecret");
		skipParameterName.put("appSecret", "appSecret");
		skipParameterName.put("sharedSecret", "sharedSecret");
		skipParameterName.put("secret", "secret");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.trace("WebXssRequestFilter");
		boolean isWebXss = false;
		HttpServletRequest request = ((HttpServletRequest) servletRequest);
		if (log.isTraceEnabled()) {
			AuthWebContext.printRequest(request);
		}
		if (skipUrlMap.containsKey(request.getRequestURI().substring(request.getContextPath().length()))) {
			isWebXss = false;
		} else {
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String key = (String) parameterNames.nextElement();
				if (skipParameterName.containsKey(key)) {
					continue;
				}

				String value = request.getParameter(key);
				log.trace("parameter name " + key + " , value " + value);
				String tempValue = value;
				if (!StringEscapeUtils.escapeHtml4(tempValue).equals(value)
						|| tempValue.toLowerCase().indexOf("script") > -1
						|| tempValue.toLowerCase().replace(" ", "").indexOf("eval(") > -1) {
					isWebXss = true;
					log.error("parameter name " + key + " , value " + value + ", contains dangerous content ! ");
					break;
				}
			}
		}
		if (!isWebXss) {
			chain.doFilter(request, response);
		}
	}
}