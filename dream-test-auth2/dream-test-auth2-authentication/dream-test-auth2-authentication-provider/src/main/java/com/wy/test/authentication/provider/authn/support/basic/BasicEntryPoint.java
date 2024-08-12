package com.wy.test.authentication.provider.authn.support.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.authentication.core.authn.LoginCredential;
import com.wy.test.authentication.provider.authn.provider.AbstractAuthenticationProvider;

import dream.flying.flower.framework.core.helper.TokenHeader;
import dream.flying.flower.framework.core.helper.TokenHelpers;
import dream.flying.flower.framework.web.enums.AuthLoginType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicEntryPoint implements AsyncHandlerInterceptor {

	boolean enable;

	@Autowired
	@Qualifier("authenticationProvider")
	AbstractAuthenticationProvider authenticationProvider;

	public BasicEntryPoint() {

	}

	public BasicEntryPoint(boolean enable) {
		super();
		this.enable = enable;
	}

	String[] skipRequestURI = { "/oauth/v20/token", "/oauth/v10a/request_token", "/oauth/v10a/access_token" };

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!enable) {
			return true;
		}
		String requestPath = request.getServletPath();
		log.debug("HttpHeader Login Start ...");
		log.info("Request url : " + request.getRequestURL());
		log.info("Request URI : " + request.getRequestURI());
		log.info("Request ContextPath : " + request.getContextPath());
		log.info("Request ServletPath : " + request.getServletPath());
		log.debug("RequestSessionId : " + request.getRequestedSessionId());
		log.debug("isRequestedSessionIdValid : " + request.isRequestedSessionIdValid());
		log.debug("getSession : " + request.getSession(false));

		for (int i = 0; i < skipRequestURI.length; i++) {
			if (skipRequestURI[i].indexOf(requestPath) > -1) {
				log.info("skip uri : " + requestPath);
				return true;
			}
		}

		// session not exists，session timeout，recreate new session
		if (request.getSession(false) == null) {
			log.info("recreate new session .");
			request.getSession(true);
		}
		String basicCredential = request.getHeader(TokenHelpers.HEADER_Authorization);
		log.info("getSession.getId : " + request.getSession().getId());

		log.info("Authorization : " + basicCredential);

		if (basicCredential == null || basicCredential.equals("")) {
			log.info("Authentication fail header Authorization is null . ");
			return false;
		}

		TokenHeader headerCredential = null;

		if (TokenHelpers.isBasic(basicCredential)) {
			headerCredential = TokenHelpers.resolve(basicCredential);
		} else {
			return false;
		}
		if (headerCredential.getUsername() == null || headerCredential.getUsername().equals("")) {
			log.info("Authentication fail username is null . ");
			return false;
		}
		if (headerCredential.getCredential() == null || headerCredential.getCredential().equals("")) {
			log.info("Authentication fail password is null . ");
			return false;
		}

		boolean isAuthenticated = false;

		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			log.info("Security Authentication  is  null .");
			isAuthenticated = false;
		} else {
			log.info("Security Authentication   not null . ");
			UsernamePasswordAuthenticationToken authenticationToken =
					(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
			String lastSessionUserName = authenticationToken.getPrincipal().toString();
			log.info("Authentication Principal : " + lastSessionUserName);
			if (lastSessionUserName != null && !lastSessionUserName.equals(headerCredential.getUsername())) {
				isAuthenticated = false;
			} else {
				isAuthenticated = true;
			}
		}

		if (!isAuthenticated) {
			LoginCredential loginCredential =
					new LoginCredential(headerCredential.getUsername(), "", AuthLoginType.BASIC);
			authenticationProvider.authenticate(loginCredential, true);
			log.info("Authentication  " + headerCredential.getUsername() + " successful .");
		}

		return true;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
