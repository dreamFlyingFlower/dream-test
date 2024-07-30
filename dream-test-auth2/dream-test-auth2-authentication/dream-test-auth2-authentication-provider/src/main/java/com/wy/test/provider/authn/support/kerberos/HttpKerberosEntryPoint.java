package com.wy.test.provider.authn.support.kerberos;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.core.authn.LoginCredential;
import com.wy.test.core.authn.web.AuthorizationUtils;
import com.wy.test.core.constants.ConstsLoginType;
import com.wy.test.core.properties.DreamServerProperties;
import com.wy.test.core.web.WebConstants;
import com.wy.test.provider.authn.provider.AbstractAuthenticationProvider;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.helper.DateTimeHelper;

public class HttpKerberosEntryPoint implements AsyncHandlerInterceptor {

	private static final Logger _logger = LoggerFactory.getLogger(HttpKerberosEntryPoint.class);

	boolean enable;

	DreamServerProperties dreamServerProperties;

	AbstractAuthenticationProvider authenticationProvider;

	KerberosService kerberosService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean isAuthenticated = AuthorizationUtils.isAuthenticated();
		String kerberosTokenString = request.getParameter(WebConstants.KERBEROS_TOKEN_PARAMETER);
		String kerberosUserDomain = request.getParameter(WebConstants.KERBEROS_USERDOMAIN_PARAMETER);

		if (!enable || isAuthenticated || kerberosTokenString == null) {
			return true;
		}

		_logger.trace("Kerberos Login Start ...");
		_logger.trace("Request url : " + request.getRequestURL());
		_logger.trace("Request URI : " + request.getRequestURI());
		_logger.trace("Request ContextPath : " + request.getContextPath());
		_logger.trace("Request ServletPath : " + request.getServletPath());
		_logger.trace("RequestSessionId : " + request.getRequestedSessionId());
		_logger.trace("isRequestedSessionIdValid : " + request.isRequestedSessionIdValid());
		_logger.trace("getSession : " + request.getSession(false));

		// session not exists，session timeout，recreate new session
		if (request.getSession(false) == null) {
			_logger.trace("recreate new session .");
			request.getSession(true);
		}

		_logger.trace("getSession.getId : " + request.getSession().getId());

		// for Kerberos Login
		_logger.debug("Try Kerberos login ");
		_logger.debug("encoder Kerberos Token " + kerberosTokenString);
		_logger.debug("kerberos UserDomain " + kerberosUserDomain);

		String decoderKerberosToken = null;
		for (KerberosProxy kerberosProxy : kerberosService.getKerberosProxys()) {
			if (kerberosProxy.getUserdomain().equalsIgnoreCase(kerberosUserDomain)) {
				decoderKerberosToken = ReciprocalHelpers.aesDecoder(kerberosTokenString, kerberosProxy.getCrypto());
				break;
			}
		}
		_logger.debug("decoder Kerberos Token " + decoderKerberosToken);
		KerberosToken kerberosToken = new KerberosToken();
		kerberosToken = (KerberosToken) JsonHelpers.read(decoderKerberosToken, kerberosToken.getClass());
		_logger.debug("Kerberos Token " + kerberosToken);

		LocalDateTime localDateTime = DateTimeHelper.toUtcDateTime(kerberosToken.getNotOnOrAfter());
		_logger.debug("Kerberos Token is After Now  " + localDateTime.isAfter(LocalDateTime.now()));

		if (localDateTime.isAfter(LocalDateTime.now())) {
			LoginCredential loginCredential =
					new LoginCredential(kerberosToken.getPrincipal(), "", ConstsLoginType.KERBEROS);
			loginCredential.setProvider(kerberosUserDomain);
			authenticationProvider.authenticate(loginCredential, true);
			_logger.debug("Kerberos Logined in , username " + kerberosToken.getPrincipal());
		}

		return true;
	}

	public HttpKerberosEntryPoint() {
		super();
	}

	public HttpKerberosEntryPoint(boolean enable) {
		super();
		this.enable = enable;
	}

	public HttpKerberosEntryPoint(AbstractAuthenticationProvider authenticationProvider,
			KerberosService kerberosService, DreamServerProperties dreamServerProperties, boolean enable) {
		super();
		this.authenticationProvider = authenticationProvider;
		this.kerberosService = kerberosService;
		this.dreamServerProperties = dreamServerProperties;
		this.enable = enable;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setApplicationConfig(DreamServerProperties dreamServerProperties) {
		this.dreamServerProperties = dreamServerProperties;
	}

	public void setAuthenticationProvider(AbstractAuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

}
