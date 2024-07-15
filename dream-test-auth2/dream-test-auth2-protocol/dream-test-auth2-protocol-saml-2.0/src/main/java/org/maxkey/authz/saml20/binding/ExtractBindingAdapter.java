package org.maxkey.authz.saml20.binding;

import java.security.KeyStore;

import javax.servlet.http.HttpServletRequest;

import org.opensaml.ws.message.MessageContext;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.CredentialResolver;

import com.wy.test.crypto.keystore.KeyStoreLoader;
import com.wy.test.entity.apps.AppsSAML20Details;

/**
 * 
 * Abstracts the SAML Binding used to send/receive messages.
 *
 */
public interface ExtractBindingAdapter {

	// public SAMLMessageContext extractSAMLMessageContext(HttpServletRequest request) throws MessageDecodingException,
	// SecurityException;
	public MessageContext extractSAMLMessageContext(HttpServletRequest request)
			throws MessageDecodingException, SecurityException;

	public String extractSAMLMessage(HttpServletRequest request);

	public void setSecurityPolicyResolver(SecurityPolicyResolver securityPolicyResolver);

	public void buildSecurityPolicyResolver(KeyStore trustKeyStore);

	public void setSaml20Detail(AppsSAML20Details saml20Detail);

	public AppsSAML20Details getSaml20Detail();

	public KeyStoreLoader getKeyStoreLoader();

	public CredentialResolver getCredentialResolver();
}
