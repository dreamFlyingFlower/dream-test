package com.wy.test.core.configuration.oidc;

import java.net.URI;
import java.util.Set;

public interface OIDCProviderMetadata {

	String getIssuer();

	void setIssuer(String issuer);

	URI getAuthorizationEndpoint();

	void setAuthorizationEndpoint(URI authorizationEndpoint);

	URI getTokenEndpoint();

	void setTokenEndpoint(URI tokenEndpoint);

	URI getUserinfoEndpoint();

	void setUserinfoEndpoint(URI userinfoEndpoint);

	URI getJwksUri();

	void setJwksUri(URI jwksUri);

	URI getRegistrationEndpoint();

	void setRegistrationEndpoint(URI registrationEndpoint);

	Set<String> getScopesSupported();

	void setScopesSupported(Set<String> scopesSupported);

	Set<String> getResponseTypesSupported();

	void setResponseTypesSupported(Set<String> responseTypesSupported);
}