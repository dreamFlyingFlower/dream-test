package com.wy.test.protocol.oauth2.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.Assert;

import com.wy.test.core.cache.MomentaryService;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.persistence.service.AppService;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;
import com.wy.test.protocol.oauth2.provider.CompositeTokenGranter;
import com.wy.test.protocol.oauth2.provider.OAuth2RequestFactory;
import com.wy.test.protocol.oauth2.provider.TokenGranter;
import com.wy.test.protocol.oauth2.provider.client.ClientCredentialsTokenGranter;
import com.wy.test.protocol.oauth2.provider.code.AuthorizationCodeServices;
import com.wy.test.protocol.oauth2.provider.code.AuthorizationCodeTokenGranter;
import com.wy.test.protocol.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import com.wy.test.protocol.oauth2.provider.implicit.ImplicitTokenGranter;
import com.wy.test.protocol.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import com.wy.test.protocol.oauth2.provider.refresh.RefreshTokenGranter;
import com.wy.test.protocol.oauth2.provider.request.DefaultOAuth2RequestFactory;
import com.wy.test.protocol.oauth2.provider.token.AuthorizationServerTokenServices;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractEndpoint implements InitializingBean {

	private TokenGranter tokenGranter;

	@Autowired
	@Qualifier("oauth20AuthorizationCodeServices")
	protected AuthorizationCodeServices authorizationCodeServices = new InMemoryAuthorizationCodeServices();

	@Autowired
	@Qualifier("oauth20TokenServices")
	protected AuthorizationServerTokenServices tokenServices;

	@Autowired
	@Qualifier("oauth20JdbcClientDetailsService")
	protected ClientDetailsService clientDetailsService;

	@Autowired
	@Qualifier("oAuth2RequestFactory")
	protected OAuth2RequestFactory oAuth2RequestFactory;

	@Autowired
	@Qualifier("oAuth2RequestFactory")
	protected OAuth2RequestFactory defaultOAuth2RequestFactory;

	@Autowired
	@Qualifier("oauth2UserAuthenticationManager")
	AuthenticationManager authenticationManager;

	@Autowired
	protected AppService appService;

	@Autowired
	protected DreamAuthServerProperties dreamServerProperties;

	@Autowired
	protected MomentaryService momentaryService;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (tokenGranter == null) {
			// ClientDetailsService clientDetails = clientDetailsService();
			// AuthorizationServerTokenServices tokenServices = tokenServices();
			// AuthorizationCodeServices authorizationCodeServices =
			// authorizationCodeServices();
			// OAuth2RequestFactory requestFactory = requestFactory();

			List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();
			tokenGranters.add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices,
					clientDetailsService, oAuth2RequestFactory));
			tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, oAuth2RequestFactory));
			ImplicitTokenGranter implicit =
					new ImplicitTokenGranter(tokenServices, clientDetailsService, oAuth2RequestFactory);
			tokenGranters.add(implicit);
			tokenGranters
					.add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, oAuth2RequestFactory));
			if (authenticationManager != null) {
				tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
						clientDetailsService, oAuth2RequestFactory));
			}
			tokenGranter = new CompositeTokenGranter(tokenGranters);
		}
		Assert.state(tokenGranter != null, "TokenGranter must be provided");
		Assert.state(clientDetailsService != null, "ClientDetailsService must be provided");
		defaultOAuth2RequestFactory = new DefaultOAuth2RequestFactory(getClientDetailsService());
		if (oAuth2RequestFactory == null) {
			oAuth2RequestFactory = defaultOAuth2RequestFactory;
		}
	}
}