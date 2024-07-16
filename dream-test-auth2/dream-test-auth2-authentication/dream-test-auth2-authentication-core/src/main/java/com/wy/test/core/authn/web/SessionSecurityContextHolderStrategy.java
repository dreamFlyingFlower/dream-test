package com.wy.test.core.authn.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

import com.wy.test.web.WebConstants;
import com.wy.test.web.WebContext;

/**
 * SecurityContext Session for Request , use
 * SecurityContextHolderAwareRequestFilter
 */
public class SessionSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

	private static final Logger _logger = LoggerFactory.getLogger(SessionSecurityContextHolderStrategy.class);

	@Override
	public void clearContext() {
		WebContext.removeAttribute(WebConstants.AUTHENTICATION);
	}

	@Override
	public SecurityContext getContext() {
		SecurityContext ctx = createEmptyContext();
		Authentication authentication = null;
		try {
			authentication = (Authentication) AuthorizationUtils.getAuthentication();
			if (authentication != null) {
				ctx.setAuthentication(authentication);
			}
		} catch (Exception e) {
			_logger.trace("a session ", e);
		}

		return ctx;
	}

	@Override
	public void setContext(SecurityContext context) {
		AuthorizationUtils.setAuthentication(context.getAuthentication());
	}

	@Override
	public SecurityContext createEmptyContext() {
		return new SecurityContextImpl();
	}

}
