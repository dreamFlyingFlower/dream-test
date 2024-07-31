package com.wy.test.provider.authn.support.wsfederation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.saml1.core.impl.AssertionImpl;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;

import dream.flying.flower.framework.web.enums.AuthLoginType;
import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpWsFederationEntryPoint implements AsyncHandlerInterceptor {

	boolean enable;

	DreamAuthLoginProperties dreamLoginProperties;

	AbstractAuthenticationProvider authenticationProvider;

	WsFederationService wsFederationService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean isAuthenticated = AuthorizationUtils.isAuthenticated();
		String wsFederationWA = request.getParameter(WsFederationConstants.WA);
		String wsFederationWResult = request.getParameter(WsFederationConstants.WRESULT);

		if (!enable || isAuthenticated || !dreamLoginProperties.isWsFederation() || wsFederationWA == null) {
			return true;
		}

		log.trace("WsFederation Login Start ...");
		log.trace("Request url : " + request.getRequestURL());
		log.trace("Request URI : " + request.getRequestURI());
		log.trace("Request ContextPath : " + request.getContextPath());
		log.trace("Request ServletPath : " + request.getServletPath());
		log.trace("RequestSessionId : " + request.getRequestedSessionId());
		log.trace("isRequestedSessionIdValid : " + request.isRequestedSessionIdValid());
		log.trace("getSession : " + request.getSession(false));

		// session not exists，session timeout，recreate new session
		if (request.getSession(false) == null) {
			log.trace("recreate new session .");
			request.getSession(true);
		}

		log.trace("getSession.getId : " + request.getSession().getId());

		// for WsFederation Login
		log.debug("WsFederation : " + wsFederationWA + " , wsFederationWResult : " + wsFederationWResult);
		if (dreamLoginProperties.isWsFederation() && StrHelper.isNotEmpty(wsFederationWA)
				&& wsFederationWA.equalsIgnoreCase(WsFederationConstants.WSIGNIN)) {
			log.debug("wresult : {}" + wsFederationWResult);

			final String wctx = request.getParameter(WsFederationConstants.WCTX);
			log.debug("wctx : {}" + wctx);

			// create credentials
			final AssertionImpl assertion = WsFederationUtils.parseTokenFromString(wsFederationWResult);
			// Validate the signature
			if (assertion != null && WsFederationUtils.validateSignature(assertion,
					wsFederationService.getWsFederationConfiguration().getSigningCertificates())) {
				final WsFederationCredential wsFederationCredential =
						WsFederationUtils.createCredentialFromToken(assertion);

				if (wsFederationCredential != null && wsFederationCredential.isValid(
						wsFederationService.getWsFederationConfiguration().getRelyingParty(),
						wsFederationService.getWsFederationConfiguration().getIdentifier(),
						wsFederationService.getWsFederationConfiguration().getTolerance())) {

					// Give the library user a chance to change the attributes as necessary
					if (wsFederationService.getWsFederationConfiguration().getAttributeMutator() != null) {
						wsFederationService.getWsFederationConfiguration().getAttributeMutator().modifyAttributes(
								wsFederationCredential.getAttributes(),
								wsFederationService.getWsFederationConfiguration().getUpnSuffix());
					}
					LoginCredential loginCredential = new LoginCredential(
							wsFederationCredential.getAttributes().get("").toString(), "", AuthLoginType.WSFEDERATION);
					authenticationProvider.authenticate(loginCredential, true);
					return true;
				} else {
					log.warn("SAML assertions are blank or no longer valid.");
				}
			} else {
				log.error("WS Requested Security Token is blank or the signature is not valid.");
			}
		}

		return true;
	}

	public HttpWsFederationEntryPoint() {
		super();
	}

	public HttpWsFederationEntryPoint(boolean enable) {
		super();
		this.enable = enable;
	}

	public HttpWsFederationEntryPoint(AbstractAuthenticationProvider authenticationProvider,
			WsFederationService wsFederationService, DreamAuthLoginProperties dreamLoginProperties, boolean enable) {
		super();
		this.authenticationProvider = authenticationProvider;
		this.wsFederationService = wsFederationService;
		this.dreamLoginProperties = dreamLoginProperties;
		this.enable = enable;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setDreamLoginProperties(DreamAuthLoginProperties dreamLoginProperties) {
		this.dreamLoginProperties = dreamLoginProperties;
	}

	public void setAuthenticationProvider(AbstractAuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	public void setWsFederationService(WsFederationService wsFederationService) {
		this.wsFederationService = wsFederationService;
	}
}