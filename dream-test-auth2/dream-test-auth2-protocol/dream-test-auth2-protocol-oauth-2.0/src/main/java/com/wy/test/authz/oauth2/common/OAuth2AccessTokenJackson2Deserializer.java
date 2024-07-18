package com.wy.test.authz.oauth2.common;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.wy.test.authz.oauth2.common.util.OAuth2Utils;

/**
 * <p>
 * Provides the ability to deserialize JSON response into an
 * {@link com.wy.test.authz.oauth2.common.OAuth2AccessToken} with jackson2 by
 * implementing {@link com.fasterxml.jackson.databind.JsonDeserializer}.
 * </p>
 * <p>
 * The expected format of the access token is defined by <a href=
 * "http://tools.ietf.org/html/draft-ietf-oauth-v2-22#section-5.1">Successful
 * Response</a>.
 * </p>
 *
 * @author Rob Winch
 * @author Brian Clozel
 * @see com.wy.test.authz.oauth2.common.OAuth2AccessTokenJackson2Serializer
 */
@SuppressWarnings("serial")
public final class OAuth2AccessTokenJackson2Deserializer extends StdDeserializer<OAuth2AccessToken> {

	public OAuth2AccessTokenJackson2Deserializer() {
		super(OAuth2AccessToken.class);
	}

	@Override
	public OAuth2AccessToken deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		String tokenValue = null;
		String tokenType = null;
		String refreshToken = null;
		Long expiresIn = null;
		Set<String> scope = null;
		Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();

		// What should occur if a parameter exists twice
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			String name = jp.getCurrentName();
			jp.nextToken();
			if (OAuth2AccessToken.ACCESS_TOKEN.equals(name)) {
				tokenValue = jp.getText();
			} else if (OAuth2AccessToken.TOKEN_TYPE.equals(name)) {
				tokenType = jp.getText();
			} else if (OAuth2AccessToken.REFRESH_TOKEN.equals(name)) {
				refreshToken = jp.getText();
			} else if (OAuth2AccessToken.EXPIRES_IN.equals(name)) {
				try {
					expiresIn = jp.getLongValue();
				} catch (JsonParseException e) {
					expiresIn = Long.valueOf(jp.getText());
				}
			} else if (OAuth2AccessToken.SCOPE.equals(name)) {
				String text = jp.getText();
				scope = OAuth2Utils.parseParameterList(text);
			} else {
				additionalInformation.put(name, jp.readValueAs(Object.class));
			}
		}

		// What should occur if a required parameter (tokenValue or tokenType) is
		// missing?

		DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(tokenValue);
		accessToken.setTokenType(tokenType);
		if (expiresIn != null) {
			accessToken.setExpiration(new Date(System.currentTimeMillis() + (expiresIn * 1000)));
		}
		if (refreshToken != null) {
			accessToken.setRefreshToken(new DefaultOAuth2RefreshToken(refreshToken));
		}
		accessToken.setScope(scope);
		accessToken.setAdditionalInformation(additionalInformation);

		return accessToken;
	}
}