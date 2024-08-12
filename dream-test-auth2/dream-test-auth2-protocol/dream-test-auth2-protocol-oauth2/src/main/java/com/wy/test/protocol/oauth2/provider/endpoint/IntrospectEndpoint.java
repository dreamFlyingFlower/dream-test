package com.wy.test.protocol.oauth2.provider.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wy.test.authentication.core.authn.SignPrincipal;
import com.wy.test.core.web.HttpResponseAdapter;
import com.wy.test.protocol.oauth2.common.OAuth2Constants;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;
import com.wy.test.protocol.oauth2.provider.OAuth2Authentication;
import com.wy.test.protocol.oauth2.provider.token.DefaultTokenServices;

import dream.flying.flower.framework.core.helper.TokenHeader;
import dream.flying.flower.framework.core.helper.TokenHelpers;
import dream.flying.flower.framework.core.json.JsonHelpers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "2-1-OAuth v2.0 API文档模块")
@Controller
@Slf4j
public class IntrospectEndpoint {

	@Autowired
	@Qualifier("oauth20JdbcClientDetailsService")
	private ClientDetailsService clientDetailsService;

	@Autowired
	@Qualifier("oauth20TokenServices")
	private DefaultTokenServices oauth20tokenServices;

	@Autowired
	ProviderManager oauth20ClientAuthenticationManager;

	@Autowired
	protected HttpResponseAdapter httpResponseAdapter;

	@Operation(summary = "OAuth 2.0 令牌验证接口", description = "请求参数access_token , header Authorization , token ",
			method = "POST,GET")
	@RequestMapping(value = OAuth2Constants.ENDPOINT.ENDPOINT_BASE + "/introspect",
			method = { RequestMethod.POST, RequestMethod.GET })
	public void introspect(HttpServletRequest request, HttpServletResponse response) {
		String access_token = TokenHelpers.resolveAccessToken(request);
		log.debug("access_token {}", access_token);

		OAuth2Authentication oAuth2Authentication = null;
		Introspection introspection = new Introspection(access_token);
		try {
			oAuth2Authentication = oauth20tokenServices.loadAuthentication(access_token);
			if (oAuth2Authentication != null) {
				String sub = "";
				// userAuthentication not null , is password or code ,
				if (oAuth2Authentication.getUserAuthentication() != null) {
					sub = ((SignPrincipal) oAuth2Authentication.getUserAuthentication().getPrincipal()).getUsername();
				} else {
					// client_credentials
					sub = oAuth2Authentication.getOAuth2Request().getClientId();
				}
				if (StringUtils.isNotBlank(sub)) {
					introspection.setSub(sub, true);
				}
			}
		} catch (OAuth2Exception e) {
			log.error("OAuth2Exception ", e);
		}

		httpResponseAdapter.write(response, JsonHelpers.toString(introspection), "json");
	}

	public boolean clientAuthenticate(TokenHeader headerCredential) {
		if (headerCredential != null) {
			UsernamePasswordAuthenticationToken authenticationToken = null;
			if (headerCredential.isBasic()) {
				if (StringUtils.isNotBlank(headerCredential.getUsername())
						&& StringUtils.isNotBlank(headerCredential.getCredential())) {
					UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
							headerCredential.getUsername(), headerCredential.getCredential());
					authenticationToken = (UsernamePasswordAuthenticationToken) oauth20ClientAuthenticationManager
							.authenticate(authRequest);
				}
			}
			if (authenticationToken != null && authenticationToken.isAuthenticated()) {
				return true;
			}
		}
		return false;
	}

	public void setOauth20tokenServices(DefaultTokenServices oauth20tokenServices) {
		this.oauth20tokenServices = oauth20tokenServices;
	}

	public class Introspection {

		String token;

		boolean active;

		String sub;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public String getSub() {
			return sub;
		}

		public void setSub(String sub, boolean active) {
			this.sub = sub;
			this.active = active;
		}

		public Introspection(String token) {
			this.token = token;
			this.active = false;
		}

		public Introspection(String token, boolean active, String sub) {
			this.token = token;
			this.active = active;
			this.sub = sub;
		}

	}

}