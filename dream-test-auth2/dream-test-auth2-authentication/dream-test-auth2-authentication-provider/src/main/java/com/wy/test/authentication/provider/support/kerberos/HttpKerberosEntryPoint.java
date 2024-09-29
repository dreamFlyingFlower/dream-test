package com.wy.test.authentication.provider.support.kerberos;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.authentication.core.entity.LoginCredential;
import com.wy.test.authentication.core.web.AuthorizationUtils;
import com.wy.test.authentication.provider.provider.AbstractAuthenticationProvider;
import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.properties.DreamAuthServerProperties;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.crypto.helper.ReciprocalHelpers;
import dream.flying.flower.framework.web.enums.AuthLoginType;
import dream.flying.flower.helper.DateTimeHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpKerberosEntryPoint implements AsyncHandlerInterceptor {

	boolean enable;

	DreamAuthServerProperties dreamServerProperties;

	AbstractAuthenticationProvider authenticationProvider;

	KerberosService kerberosService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean isAuthenticated = AuthorizationUtils.isAuthenticated();
		String kerberosTokenString = request.getParameter(ConstAuthWeb.KERBEROS_TOKEN_PARAMETER);
		String kerberosUserDomain = request.getParameter(ConstAuthWeb.KERBEROS_USERDOMAIN_PARAMETER);

		if (!enable || isAuthenticated || kerberosTokenString == null) {
			return true;
		}

		log.trace("Kerberos Login Start ...");
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

		// for Kerberos Login
		log.debug("Try Kerberos login ");
		log.debug("encoder Kerberos Token " + kerberosTokenString);
		log.debug("kerberos UserDomain " + kerberosUserDomain);

		String decoderKerberosToken = null;
		for (KerberosProxy kerberosProxy : kerberosService.getKerberosProxys()) {
			if (kerberosProxy.getUserdomain().equalsIgnoreCase(kerberosUserDomain)) {
				decoderKerberosToken = ReciprocalHelpers.aesDecoder(kerberosTokenString, kerberosProxy.getCrypto());
				break;
			}
		}
		log.debug("decoder Kerberos Token " + decoderKerberosToken);
		KerberosToken kerberosToken = new KerberosToken();
		kerberosToken = (KerberosToken) JsonHelpers.read(decoderKerberosToken, kerberosToken.getClass());
		log.debug("Kerberos Token " + kerberosToken);

		LocalDateTime localDateTime = DateTimeHelper.toUtcDateTime(kerberosToken.getNotOnOrAfter());
		log.debug("Kerberos Token is After Now  " + localDateTime.isAfter(LocalDateTime.now()));

		if (localDateTime.isAfter(LocalDateTime.now())) {
			LoginCredential loginCredential =
					new LoginCredential(kerberosToken.getPrincipal(), "", AuthLoginType.KERBEROS.name());
			loginCredential.setProvider(kerberosUserDomain);
			authenticationProvider.authenticate(loginCredential, true);
			log.debug("Kerberos Logined in , username " + kerberosToken.getPrincipal());
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
			KerberosService kerberosService, DreamAuthServerProperties dreamServerProperties, boolean enable) {
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

	public void setApplicationConfig(DreamAuthServerProperties dreamServerProperties) {
		this.dreamServerProperties = dreamServerProperties;
	}

	public void setAuthenticationProvider(AbstractAuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}
}