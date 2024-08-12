package com.wy.test.core.entity.oidc;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OIDCProviderMetadataDetails.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OidcProviderMetadataDetail implements OidcProviderMetadata {

	protected String issuer;

	protected URI authorizationEndpoint;

	protected URI tokenEndpoint;

	protected URI userinfoEndpoint;

	protected URI jwksUri;

	protected URI registrationEndpoint;

	protected Set<String> scopesSupported;

	protected Set<String> responseTypesSupported;


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