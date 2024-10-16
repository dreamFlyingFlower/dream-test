package com.wy.test.protocol.oauth2.endpoint;

import java.util.Map;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.protocol.oauth2.common.OAuth2AccessToken;
import com.wy.test.protocol.oauth2.common.OAuth2Constants;
import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;
import com.wy.test.protocol.oauth2.provider.token.AccessTokenConverter;
import com.wy.test.protocol.oauth2.provider.token.DefaultAccessTokenConverter;
import com.wy.test.protocol.oauth2.provider.token.ResourceServerTokenServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Controller which decodes access tokens for clients who are not able to do so
 * (or where opaque token values are used).
 * 
 * @author Luke Taylor
 * @author Joel D'sa
 */
@Tag(name = "OAuth2.0 Token验证API")
@Setter
@RestController
@RequiredArgsConstructor
public class CheckTokenEndpoint {

	private final ResourceServerTokenServices resourceServerTokenServices;

	private AccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

	@Operation(summary = "Token检查", description = "传递参数token", method = "POST")
	@PostMapping(value = OAuth2Constants.ENDPOINT.ENDPOINT_CHECK_TOKEN)
	public Map<String, ?> checkToken(@RequestParam(OAuth2Constants.PARAMETER.TOKEN) String value) {
		OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
		if (token == null) {
			throw new InvalidTokenException("Token was not recognised");
		}
		if (token.isExpired()) {
			throw new InvalidTokenException("Token has expired");
		}

		OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());
		Map<String, ?> response = accessTokenConverter.convertAccessToken(token, authentication);
		return response;
	}
}