package com.wy.test.authentication.provider.authn.support.certs;

import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.wy.test.authentication.provider.authn.provider.AbstractAuthenticationProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpCertsEntryPoint implements AsyncHandlerInterceptor {

	static String CERTIFICATE_ATTRIBUTE = "javax.servlet.request.X509Certificate";

	static String PEER_CERTIFICATES_ATTRIBUTE = "javax.net.ssl.peer_certificates";

	boolean enable;

	@Autowired
	@Qualifier("authenticationProvider")
	AbstractAuthenticationProvider authenticationProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!enable) {
			return true;
		}

		log.debug("Certificate Login Start ...");
		log.debug("Request url : " + request.getRequestURL());
		log.debug("Request URI : " + request.getRequestURI());
		log.trace("Request ContextPath : " + request.getContextPath());
		log.trace("Request ServletPath : " + request.getServletPath());
		log.trace("RequestSessionId : " + request.getRequestedSessionId());
		log.trace("isRequestedSessionIdValid : " + request.isRequestedSessionIdValid());
		log.trace("getSession : " + request.getSession(false));

		X509Certificate[] certificates = (X509Certificate[]) request.getAttribute(CERTIFICATE_ATTRIBUTE); // 2.2 spec
		if (certificates == null) {
			certificates = (X509Certificate[]) request.getAttribute(PEER_CERTIFICATES_ATTRIBUTE); // 2.1 spec
		}

		for (X509Certificate cert : certificates) {
			cert.checkValidity();
			log.debug("cert validated");
			log.debug("cert infos " + cert.toString());
			log.debug("Version " + cert.getVersion());
			log.debug("SerialNumber " + cert.getSerialNumber().toString(16));
			log.debug("SubjectDN " + cert.getSubjectDN());
			log.debug("IssuerDN " + cert.getIssuerDN());
			log.debug("NotBefore " + cert.getNotBefore());
			log.debug("SigAlgName " + cert.getSigAlgName());
			byte[] sign = cert.getSignature();
			log.debug("Signature ");
			for (int j = 0; j < sign.length; j++) {
				log.debug(sign[j] + ",");
			}
			java.security.PublicKey pk = cert.getPublicKey();
			byte[] pkenc = pk.getEncoded();
			log.debug("PublicKey ");
			for (int j = 0; j < pkenc.length; j++) {
				log.debug(pkenc[j] + ",");
			}
		}
		return true;
	}

	public HttpCertsEntryPoint(boolean enable, AbstractAuthenticationProvider authenticationProvider) {
		super();
		this.enable = enable;
		this.authenticationProvider = authenticationProvider;
	}

}
