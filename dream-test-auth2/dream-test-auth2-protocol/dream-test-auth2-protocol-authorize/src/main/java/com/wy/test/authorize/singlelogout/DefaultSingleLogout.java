package com.wy.test.authorize.singlelogout;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.security.core.Authentication;

import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.entity.apps.Apps;
import com.wy.test.util.DateUtils;

public class DefaultSingleLogout extends SingleLogout {

	@Override
	public void sendRequest(Authentication authentication, Apps logoutApp) {
		HashMap<String, Object> logoutParameters = new HashMap<String, Object>();
		logoutParameters.put("id", UUID.randomUUID().toString());
		logoutParameters.put("principal", authentication.getName());
		logoutParameters.put("request", "logoutRequest");
		logoutParameters.put("issueInstant", DateUtils.getCurrentDateAsString(DateUtils.FORMAT_DATE_ISO_TIMESTAMP));
		logoutParameters.put("ticket", ((SignPrincipal) authentication.getPrincipal()).getSession().getFormattedId());
		postMessage(logoutApp.getLogoutUrl(), logoutParameters);

	}

}
