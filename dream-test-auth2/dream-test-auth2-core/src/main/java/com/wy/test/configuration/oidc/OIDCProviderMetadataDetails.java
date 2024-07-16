package com.wy.test.configuration.oidc;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * OIDCProviderMetadataDetails.
 * 
 * @author cm
 *
 */
public class OIDCProviderMetadataDetails implements OIDCProviderMetadata {

	protected String issuer;

	protected URI authorizationEndpoint;

	protected URI tokenEndpoint;

	protected URI userinfoEndpoint;

	protected URI jwksUri;

	protected URI registrationEndpoint;

	protected Set<String> scopesSupported;

	protected Set<String> responseTypesSupported;

	@Override
	public String getIssuer() {
		return issuer;
	}

	@Override
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	@Override
	public URI getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}

	@Override
	public void setAuthorizationEndpoint(URI authorizationEndpoint) {
		this.authorizationEndpoint = authorizationEndpoint;
	}

	@Override
	public URI getTokenEndpoint() {
		return tokenEndpoint;
	}

	@Override
	public void setTokenEndpoint(URI tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}

	@Override
	public URI getUserinfoEndpoint() {
		return userinfoEndpoint;
	}

	@Override
	public void setUserinfoEndpoint(URI userinfoEndpoint) {
		this.userinfoEndpoint = userinfoEndpoint;
	}

	@Override
	public URI getJwksUri() {
		return jwksUri;
	}

	@Override
	public void setJwksUri(URI jwksUri) {
		this.jwksUri = jwksUri;
	}

	@Override
	public URI getRegistrationEndpoint() {
		return registrationEndpoint;
	}

	@Override
	public void setRegistrationEndpoint(URI registrationEndpoint) {
		this.registrationEndpoint = registrationEndpoint;
	}

	@Override
	public Set<String> getScopesSupported() {
		return scopesSupported;
	}

	@Override
	public void setScopesSupported(Set<String> scopesSupported) {
		this.scopesSupported = scopesSupported;
	}

	@Override
	public Set<String> getResponseTypesSupported() {
		return responseTypesSupported;
	}

	@Override
	public void setResponseTypesSupported(Set<String> responseTypesSupported) {
		this.responseTypesSupported = responseTypesSupported;
	}

	@Override
	public String toString() {
		final int maxLen = 4;
		StringBuilder builder = new StringBuilder();
		builder.append("OIDCProviderMetadataDetails [issuer=");
		builder.append(issuer);
		builder.append(", authorizationEndpoint=");
		builder.append(authorizationEndpoint);
		builder.append(", tokenEndpoint=");
		builder.append(tokenEndpoint);
		builder.append(", userinfoEndpoint=");
		builder.append(userinfoEndpoint);
		builder.append(", jwksUri=");
		builder.append(jwksUri);
		builder.append(", registrationEndpoint=");
		builder.append(registrationEndpoint);
		builder.append(", scopesSupported=");
		builder.append(scopesSupported != null ? toString(scopesSupported, maxLen) : null);
		builder.append(", responseTypesSupported=");
		builder.append(responseTypesSupported != null ? toString(responseTypesSupported, maxLen) : null);
		builder.append("]");
		return builder.toString();
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	// Complete remaining properties from
	// http://openid.net/specs/openid-connect-discovery-1_0.html#ProviderMetadata

}
