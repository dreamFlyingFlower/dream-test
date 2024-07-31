package com.wy.test.provider.authn.support.httpheader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpHeaderEntryPoint implements AsyncHandlerInterceptor {

	String headerName;

	boolean enable;

	@Autowired
	@Qualifier("authenticationProvider")
	AbstractAuthenticationProvider authenticationProvider;

	String[] skipRequestURI = { "/oauth/v20/token", "/oauth/v10a/request_token", "/oauth/v10a/access_token" };

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!enable) {
			return true;
		}
		String requestPath = request.getServletPath();
		log.trace("HttpHeader Login Start ...");
		log.trace("Request url : " + request.getRequestURL());
		log.trace("Request URI : " + request.getRequestURI());
		log.trace("Request ContextPath : " + request.getContextPath());
		log.trace("Request ServletPath : " + request.getServletPath());
		log.trace("RequestSessionId : " + request.getRequestedSessionId());
		log.trace("isRequestedSessionIdValid : " + request.isRequestedSessionIdValid());
		log.trace("getSession : " + request.getSession(false));

		for (int i = 0; i < skipRequestURI.length; i++) {
			if (skipRequestURI[i].indexOf(requestPath) > -1) {
				log.trace("skip uri : " + requestPath);
				return true;
			}
		}

		// session not exists，session timeout，recreate new session
		if (request.getSession(false) == null) {
			log.trace("recreate new session .");
			request.getSession(true);
		}

		log.trace("getSession.getId : " + request.getSession().getId());
		String httpHeaderUsername = request.getHeader(headerName);

		log.trace("HttpHeader username : " + httpHeaderUsername);

		if (httpHeaderUsername == null || httpHeaderUsername.equals("")) {
			log.info("Authentication fail HttpHeader is null . ");
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
			if (lastSessionUserName != null && !lastSessionUserName.equals(httpHeaderUsername)) {
				isAuthenticated = false;
			} else {
				isAuthenticated = true;
			}
		}

		if (!isAuthenticated) {
			LoginCredential loginCredential = new LoginCredential(httpHeaderUsername, "", AuthLoginType.HTTPHEADER);
			authenticationProvider.authenticate(loginCredential, true);
			log.info("Authentication  " + httpHeaderUsername + " successful .");
		}

		return true;
	}

	public HttpHeaderEntryPoint() {
		super();
	}

	public HttpHeaderEntryPoint(String headerName, boolean enable) {
		super();
		this.headerName = headerName;
		this.enable = enable;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}