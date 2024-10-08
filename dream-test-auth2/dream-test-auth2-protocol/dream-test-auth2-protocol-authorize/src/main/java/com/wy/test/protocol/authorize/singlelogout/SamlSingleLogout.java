package com.wy.test.protocol.authorize.singlelogout;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.security.core.Authentication;

import com.wy.test.core.vo.AppVO;

import dream.flying.flower.ConstDate;
import dream.flying.flower.helper.DateTimeHelper;

/**
 * SamlSingleLogout
 * https://apereo.github.io/cas/6.5.x/installation/Logout-Single-Signout.html
 */
public class SamlSingleLogout extends SingleLogout {

	/**
	 * The parameter name that contains the logout request.
	 */
	public static final String LOGOUT_REQUEST_PARAMETER = "logoutRequest";

	public static final String logoutRequestMessage =
			"<samlp:LogoutRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\"%s\" Version=\"2.0\" "
					+ "IssueInstant=\"%s\"><saml:NameID xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">%s"
					+ "</saml:NameID><samlp:SessionIndex>%s</samlp:SessionIndex></samlp:LogoutRequest>";

	@Override
	public void sendRequest(Authentication authentication, AppVO logoutApp) {
		String requestMessage = String.format(logoutRequestMessage, UUID.randomUUID().toString(),
				DateTimeHelper.format(ConstDate.DATETIME_ISO), authentication.getName(), logoutApp.getOnlineTicket());

		HashMap<String, Object> logoutParameters = new HashMap<String, Object>();
		logoutParameters.put(LOGOUT_REQUEST_PARAMETER, requestMessage);
		postMessage(logoutApp.getLogoutUrl(), logoutParameters);
	}

	public SamlSingleLogout() {
		super();
	}

}
