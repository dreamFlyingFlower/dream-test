package com.wy.test.protocol.saml2.saml20.binding.impl;

import java.security.KeyStore;

import org.opensaml.common.binding.decoding.SAMLMessageDecoder;
import org.opensaml.ws.security.SecurityPolicyResolver;
import org.opensaml.xml.security.credential.CredentialResolver;

import com.wy.test.protocol.saml2.saml.common.TrustResolver;

public class ExtractRedirectBindingAdapter extends ExtractPostBindingAdapter {

	public ExtractRedirectBindingAdapter(SAMLMessageDecoder decoder) {
		this.decoder = decoder;
	}

	public ExtractRedirectBindingAdapter(SAMLMessageDecoder decoder, String issuingEntityName) {
		this.decoder = decoder;
		this.issuingEntityName = issuingEntityName;
	}

	public ExtractRedirectBindingAdapter(SAMLMessageDecoder decoder, String issuingEntityName,
			SecurityPolicyResolver securityPolicyResolver) {
		this.decoder = decoder;
		this.issuingEntityName = issuingEntityName;

		this.securityPolicyResolver = securityPolicyResolver;
	}

	@Override
	public void buildSecurityPolicyResolver(KeyStore trustKeyStore) {

		TrustResolver trustResolver = new TrustResolver(trustKeyStore, keyStoreLoader.getEntityName(),
				keyStoreLoader.getKeystorePassword(), issueInstantRule, messageReplayRule, "Redirect");
		credentialResolver = (CredentialResolver) trustResolver.getKeyStoreCredentialResolver();
		this.securityPolicyResolver = trustResolver.getStaticSecurityPolicyResolver();
	}

}
