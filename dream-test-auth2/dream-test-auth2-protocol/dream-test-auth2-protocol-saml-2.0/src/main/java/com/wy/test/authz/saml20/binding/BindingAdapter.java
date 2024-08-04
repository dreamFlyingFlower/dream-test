package com.wy.test.authz.saml20.binding;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.common.SignableSAMLObject;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.xml.security.credential.Credential;

import com.wy.test.authz.saml.common.AuthnRequestInfo;
import com.wy.test.core.entity.AppSamlDetailEntity;

/**
 * 
 * Abstracts the SAML Binding used to send/receive messages.
 */
public interface BindingAdapter {

	void sendSAMLMessage(SignableSAMLObject samlMessage, Endpoint endpoint, HttpServletRequest request,
			HttpServletResponse response) throws MessageEncodingException;

	void setSecurityPolicyResolver(SecurityPolicyResolver securityPolicyResolver);

	void setExtractBindingAdapter(ExtractBindingAdapter extractBindingAdapter);

	void setAuthnRequestInfo(AuthnRequestInfo authnRequestInfo);

	void setRelayState(String relayState);

	AppSamlDetailEntity getSaml20Details();

	AuthnRequestInfo getAuthnRequestInfo();

	Credential getSigningCredential();

	Credential getSpSigningCredential();
}