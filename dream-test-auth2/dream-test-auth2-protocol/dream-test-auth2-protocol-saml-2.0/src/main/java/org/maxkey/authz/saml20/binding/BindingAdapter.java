package org.maxkey.authz.saml20.binding;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.maxkey.authz.saml.common.AuthnRequestInfo;
import org.opensaml.saml.common.SignableSAMLObject;
import org.opensaml.saml.saml2.metadata.Endpoint;
import org.opensaml.security.credential.Credential;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;

import com.wy.test.entity.apps.AppsSAML20Details;

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

	AppsSAML20Details getSaml20Details();

	AuthnRequestInfo getAuthnRequestInfo();

	Credential getSigningCredential();

	Credential getSpSigningCredential();
}