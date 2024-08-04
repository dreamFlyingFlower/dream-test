package com.wy.test.core.authn.web;

import java.text.ParseException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.authn.session.Session;
import com.wy.test.core.authn.session.SessionManager;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.framework.core.helper.TokenHelpers;

public class AuthorizationUtils {

	private static final Logger _logger = LoggerFactory.getLogger(AuthorizationUtils.class);

	public static final class BEARERTYPE {

		public static final String CONGRESS = "congress";

		public static final String AUTHORIZATION = "Authorization";
	}

	public static void authenticateWithCookie(HttpServletRequest request, AuthTokenService authTokenService,
			SessionManager sessionManager) throws ParseException {
		Cookie authCookie = WebContext.getCookie(request, BEARERTYPE.CONGRESS);
		if (authCookie != null) {
			String authorization = authCookie.getValue();
			_logger.trace("Try congress authenticate .");
			doJwtAuthenticate(BEARERTYPE.CONGRESS, authorization, authTokenService, sessionManager);
		} else {
			_logger.debug("cookie is null , clear authentication .");
			clearAuthentication();
		}
	}

	public static void authenticate(HttpServletRequest request, AuthTokenService authTokenService,
			SessionManager sessionManager) throws ParseException {
		String authorization = TokenHelpers.resolveBearer(request);
		if (authorization != null) {
			_logger.trace("Try Authorization authenticate .");
			doJwtAuthenticate(BEARERTYPE.AUTHORIZATION, authorization, authTokenService, sessionManager);
		}

	}

	public static void doJwtAuthenticate(String bearerType, String authorization, AuthTokenService authTokenService,
			SessionManager sessionManager) throws ParseException {
		if (authTokenService.validateJwtToken(authorization)) {
			if (isNotAuthenticated()) {
				String sessionId = authTokenService.resolveJWTID(authorization);
				Session session = sessionManager.get(sessionId);
				if (session != null) {
					setAuthentication(session.getAuthentication());
					_logger.debug("{} Automatic authenticated .", bearerType);
				} else {
					// time out
					_logger.debug("Session timeout .");
					clearAuthentication();
				}
			}
		} else {
			// token invalidate
			_logger.debug("Token invalidate .");
			clearAuthentication();
		}
	}

	public static Authentication getAuthentication() {
		Authentication authentication = (Authentication) getAuthentication(WebContext.getRequest());
		return authentication;
	}

	public static Authentication getAuthentication(HttpServletRequest request) {
		Authentication authentication = (Authentication) request.getSession().getAttribute(WebConstants.AUTHENTICATION);
		return authentication;
	}

	// set Authentication to http session
	public static void setAuthentication(Authentication authentication) {
		WebContext.setAttribute(WebConstants.AUTHENTICATION, authentication);
	}

	public static void clearAuthentication() {
		WebContext.removeAttribute(WebConstants.AUTHENTICATION);
	}

	public static boolean isAuthenticated() {
		return getAuthentication() != null;
	}

	public static boolean isNotAuthenticated() {
		return !isAuthenticated();
	}

	public static SignPrincipal getPrincipal() {
		Authentication authentication = getAuthentication();
		return getPrincipal(authentication);
	}

	public static SignPrincipal getPrincipal(Authentication authentication) {
		return authentication == null ? null : (SignPrincipal) authentication.getPrincipal();
	}

	public static UserVO getUserInfo(Authentication authentication) {
		UserVO userInfo = null;
		SignPrincipal principal = getPrincipal(authentication);
		if (principal != null) {
			userInfo = principal.getUserInfo();
		}
		return userInfo;
	}

	public static UserVO getUserInfo() {
		return getUserInfo(getAuthentication());
	}
}