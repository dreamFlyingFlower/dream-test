package com.wy.test.protocol.saml2.saml20.binding;

import java.security.KeyStore;

import javax.servlet.http.HttpServletRequest;

import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.CredentialResolver;

import com.wy.test.core.vo.AppSamlDetailVO;

import dream.flying.flower.framework.crypto.keystore.KeyStoreLoader;

/**
 * Abstracts the SAML Binding used to send/receive messages.
 */
public interface ExtractBindingAdapter {

	@SuppressWarnings("rawtypes")
	SAMLMessageContext extractSAMLMessageContext(HttpServletRequest request)
			throws MessageDecodingException, SecurityException;

	String extractSAMLMessage(HttpServletRequest request);

	void setSecurityPolicyResolver(SecurityPolicyResolver securityPolicyResolver);

	void buildSecurityPolicyResolver(KeyStore trustKeyStore);

	void setSaml20Detail(AppSamlDetailVO saml20Detail);

	AppSamlDetailVO getSaml20Detail();

	KeyStoreLoader getKeyStoreLoader();

	CredentialResolver getCredentialResolver();
}