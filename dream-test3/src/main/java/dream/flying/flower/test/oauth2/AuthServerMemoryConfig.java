package dream.flying.flower.test.oauth2;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * SpringSecurity5.7认证服务器-基于内存,其余配置参照{@link AuthServerConfig},需要剔除数据库配置
 * 
 * @author 飞花梦影
 * @date 2024-09-18 21:36:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class AuthServerMemoryConfig {

	/**
	 * 客户端配置,基于内存
	 */
	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate,
			PasswordEncoder passwordEncoder) {
		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("clientId")
				.clientName("clientName")
				.clientIdIssuedAt(Instant.now())
				.clientSecret(passwordEncoder.encode("123456"))
				.clientSecretExpiresAt(Instant.now().plus(14, ChronoUnit.DAYS))
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.postLogoutRedirectUri("http://127.0.0.1:8080/logout")
				.scope("message.read")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
				.tokenSettings(TokenSettings.builder()
						// access token 有效期
						.accessTokenTimeToLive(Duration.ofMinutes(60))
						.build())
				.build();
		return new InMemoryRegisteredClientRepository(registeredClient);
	}

	/**
	 * 基于内存的用户验证
	 * 
	 * @return 用户信息
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withUsername("user").password("password").roles("USER").build();
		return new InMemoryUserDetailsManager(userDetails);
	}
}