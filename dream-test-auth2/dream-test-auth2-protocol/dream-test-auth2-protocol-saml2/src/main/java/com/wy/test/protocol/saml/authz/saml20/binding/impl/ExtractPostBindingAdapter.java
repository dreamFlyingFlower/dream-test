package com.wy.test.protocol.saml.authz.saml20.binding.impl;

import java.security.KeyStore;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.common.binding.security.IssueInstantRule;
import org.opensaml.common.binding.security.MessageReplayRule;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.CredentialResolver;
import org.springframework.beans.factory.InitializingBean;

import com.wy.test.core.vo.AppSamlDetailVO;
import com.wy.test.protocol.saml.authz.saml.common.TrustResolver;
import com.wy.test.protocol.saml.authz.saml20.binding.ExtractBindingAdapter;

import dream.flying.flower.framework.web.crypto.keystore.KeyStoreLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtractPostBindingAdapter implements ExtractBindingAdapter, InitializingBean {

	static final String SAML_REQUEST_POST_PARAM_NAME = "SAMLRequest";

	static final String SAML_RESPONSE_POST_PARAM_NAME = "SAMLResponse";

	protected SAMLMessageDecoder decoder;

	protected String issuingEntityName;

	protected SecurityPolicyResolver securityPolicyResolver;

	protected IssueInstantRule issueInstantRule;

	protected MessageReplayRule messageReplayRule;

	protected KeyStoreLoader keyStoreLoader;

	protected CredentialResolver credentialResolver;

	protected AppSamlDetailVO saml20Detail;

	public ExtractPostBindingAdapter() {

	}

	public ExtractPostBindingAdapter(SAMLMessageDecoder decoder) {
		super();
		this.decoder = decoder;
	}

	public ExtractPostBindingAdapter(SAMLMessageDecoder decoder, String issuingEntityName) {
		super();
		this.decoder = decoder;
		this.issuingEntityName = issuingEntityName;
	}

	public ExtractPostBindingAdapter(SAMLMessageDecoder decoder, String issuingEntityName,
			SecurityPolicyResolver securityPolicyResolver) {
		super();
		this.decoder = decoder;
		this.issuingEntityName = issuingEntityName;

		this.securityPolicyResolver = securityPolicyResolver;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public SAMLMessageContext extractSAMLMessageContext(HttpServletRequest request)
			throws MessageDecodingException, SecurityException {

		BasicSAMLMessageContext messageContext = new BasicSAMLMessageContext();

		messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(request));

		messageContext.setSecurityPolicyResolver(securityPolicyResolver);

		decoder.decode(messageContext);
		log.debug("decode successed ");
		return messageContext;

	}

	@Override
	public String extractSAMLMessage(HttpServletRequest request) {

		if (StringUtils.isNotBlank(request.getParameter(SAML_REQUEST_POST_PARAM_NAME)))
			return request.getParameter(SAML_REQUEST_POST_PARAM_NAME);
		else
			return request.getParameter(SAML_RESPONSE_POST_PARAM_NAME);

	}

	@Override
	public void buildSecurityPolicyResolver(KeyStore trustKeyStore) {
		log.debug("EntityName {}, KeystorePassword {}", keyStoreLoader.getEntityName(),
				keyStoreLoader.getKeystorePassword());

		TrustResolver trustResolver = new TrustResolver(trustKeyStore, keyStoreLoader.getEntityName(),
				keyStoreLoader.getKeystorePassword(), issueInstantRule, messageReplayRule, "POST");
		credentialResolver = (CredentialResolver) trustResolver.getKeyStoreCredentialResolver();
		this.securityPolicyResolver = trustResolver.getStaticSecurityPolicyResolver();
	}

	/**
	 * @param securityPolicyResolver the securityPolicyResolver to set
	 */
	@Override
	public void setSecurityPolicyResolver(SecurityPolicyResolver securityPolicyResolver) {
		this.securityPolicyResolver = securityPolicyResolver;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void setSaml20Detail(AppSamlDetailVO saml20Detail) {
		this.saml20Detail = saml20Detail;
	}

	@Override
	public AppSamlDetailVO getSaml20Detail() {
		return saml20Detail;
	}

	@Override
	public KeyStoreLoader getKeyStoreLoader() {
		return keyStoreLoader;
	}

	public void setKeyStoreLoader(KeyStoreLoader keyStoreLoader) {
		this.keyStoreLoader = keyStoreLoader;
	}

	@Override
	public CredentialResolver getCredentialResolver() {
		return this.credentialResolver;
	}

	public void setIssuingEntityName(String issuingEntityName) {
		this.issuingEntityName = issuingEntityName;
	}

	public void setIssueInstantRule(IssueInstantRule issueInstantRule) {
		this.issueInstantRule = issueInstantRule;
	}

	public void setMessageReplayRule(MessageReplayRule messageReplayRule) {
		this.messageReplayRule = messageReplayRule;
	}

}
