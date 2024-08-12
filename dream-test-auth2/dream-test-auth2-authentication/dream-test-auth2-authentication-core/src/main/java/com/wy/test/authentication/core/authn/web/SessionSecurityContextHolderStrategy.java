package com.wy.test.authentication.core.authn.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

import com.wy.test.core.web.WebConstants;
import com.wy.test.core.web.WebContext;

import lombok.extern.slf4j.Slf4j;

/**
 * SecurityContext Session for Request , use SecurityContextHolderAwareRequestFilter
 */
@Slf4j
public class SessionSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

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
			log.trace("a session ", e);
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