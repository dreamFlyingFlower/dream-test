package com.wy.test.protocol.oauth2.common;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Provides the ability to serialize an
 * {@link com.wy.test.protocol.oauth2.common.OAuth2AccessToken} with jackson2 by
 * implementing {@link com.fasterxml.jackson.databind.JsonDeserializer}. Refer
 * to
 * {@link com.wy.authz.oauth2.common.OAuth2AccessTokenJackson1Deserializer}
 * to learn more about the JSON format that is used.
 *
 * @author Rob Winch
 * @author Brian Clozel
 * @see com.wy.test.protocol.oauth2.common.OAuth2AccessTokenJackson2Deserializer
 */
public final class OAuth2AccessTokenJackson2Serializer extends StdSerializer<OAuth2AccessToken> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7323248504425950254L;

	public OAuth2AccessTokenJackson2Serializer() {
		super(OAuth2AccessToken.class);
	}

	@Override
	public void serialize(OAuth2AccessToken token, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonGenerationException {
		jgen.writeStartObject();
		if (token.getOAuth2Exception() == null) {
			jgen.writeStringField(OAuth2AccessToken.ACCESS_TOKEN, token.getValue());
			jgen.writeStringField(OAuth2AccessToken.TOKEN_TYPE, token.getTokenType());
			OAuth2RefreshToken refreshToken = token.getRefreshToken();
			if (refreshToken != null) {
				jgen.writeStringField(OAuth2AccessToken.REFRESH_TOKEN, refreshToken.getValue());
			}
			Date expiration = token.getExpiration();
			if (expiration != null) {
				long now = System.currentTimeMillis();
				jgen.writeNumberField(OAuth2AccessToken.EXPIRES_IN, (expiration.getTime() - now) / 1000);
			}
			Set<String> scope = token.getScope();
			if (scope != null && !scope.isEmpty()) {
				StringBuffer scopes = new StringBuffer();
				for (String s : scope) {
					Assert.hasLength(s, "Scopes cannot be null or empty. Got " + scope + "");
					scopes.append(s);
					scopes.append(" ");
				}
				jgen.writeStringField(OAuth2AccessToken.SCOPE, scopes.substring(0, scopes.length() - 1));
			}
		} else {
			jgen.writeStringField(OAuth2AccessToken.ERROR, token.getOAuth2Exception().getOAuth2ErrorCode());
			jgen.writeStringField(OAuth2AccessToken.ERROR_DESCRIPTION, token.getOAuth2Exception().getMessage());
		}
		Map<String, Object> additionalInformation = token.getAdditionalInformation();
		for (String key : additionalInformation.keySet()) {
			jgen.writeObjectField(key, additionalInformation.get(key));
		}
		jgen.writeEndObject();
	}
}