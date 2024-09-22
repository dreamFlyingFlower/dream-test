package dream.flying.flower.test.oauth2;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.SneakyThrows;

/**
 * SpringSecurity5.7认证服务器
 *
 * @author 飞花梦影
 * @date 2024-09-18 21:36:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class AuthServerConfig {

	private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

	/**
	 * 配置端点的过滤器链
	 *
	 * @param http spring security核心配置类
	 * @return 过滤器链
	 * @throws Exception 抛出
	 */
	@Bean
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			RegisteredClientRepository registeredClientRepository,
			AuthorizationServerSettings authorizationServerSettings) throws Exception {
		// 配置默认的设置,忽略认证端点的csrf校验
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		// 使用redis存储、读取登录的认证信息
		// http.securityContext(context ->
		// context.securityContextRepository(redisSecurityContextRepository));

		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				// 开启OpenID Connect 1.0协议相关端点
				.oidc(Customizer.withDefaults())
				// 设置自定义用户确认授权页
				.authorizationEndpoint(
						authorizationEndpoint -> authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
				// 设置设备码用户验证url(自定义用户验证页)
				.deviceAuthorizationEndpoint(
						deviceAuthorizationEndpoint -> deviceAuthorizationEndpoint.verificationUri("/activate"))
				// 设置验证设备码用户确认页面
				.deviceVerificationEndpoint(
						deviceVerificationEndpoint -> deviceVerificationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));
		http
				// 当未登录时访问认证端点时重定向至login页面
				.exceptionHandling((exceptions) -> exceptions
						.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
				// 处理使用access token访问用户信息端点和客户端注册端点
				.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()));

		return http.build();
	}

	/**
	 * 配置认证相关的过滤器链
	 *
	 * @param http spring security核心配置类
	 * @return 过滤器链
	 * @throws Exception 抛出
	 */
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		// 添加跨域过滤器
		http.addFilter(corsFilter());
		// 禁用 csrf 与 cors
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests((authorize) -> authorize
				// 放行静态资源
				.requestMatchers("/assets/**", "/webjars/**", "/login", "/getCaptcha", "/getSmsCaptcha")
				.permitAll()
				.anyRequest()
				.authenticated());

		http
				// 当未登录时访问认证端点时重定向至login页面
				.exceptionHandling((exceptions) -> exceptions
						.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));

		// 使用redis存储、读取登录的认证信息
		// http.securityContext(context ->
		// context.securityContextRepository(redisSecurityContextRepository));

		return http.build();
	}

	/**
	 * 跨域过滤器配置
	 *
	 * @return CorsFilter
	 */
	@Bean
	public CorsFilter corsFilter() {

		// 初始化cors配置对象
		CorsConfiguration configuration = new CorsConfiguration();

		// 设置允许跨域的域名,如果允许携带cookie的话,路径就不能写*号, *表示所有的域名都可以跨域访问
		configuration.addAllowedOrigin("http://127.0.0.1:5173");
		// 设置跨域访问可以携带cookie
		configuration.setAllowCredentials(true);
		// 允许所有的请求方法 ==> GET POST PUT Delete
		configuration.addAllowedMethod("*");
		// 允许携带任何头信息
		configuration.addAllowedHeader("*");

		// 初始化cors配置源对象
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();

		// 给配置源对象设置过滤的参数
		// 参数一: 过滤的路径 == > 所有的路径都要求校验是否跨域
		// 参数二: 配置类
		configurationSource.registerCorsConfiguration("/**", configuration);

		// 返回配置好的过滤器
		return new CorsFilter(configurationSource);
	}

	/**
	 * 自定义jwt,将权限信息放至jwt中
	 *
	 * @return OAuth2TokenCustomizer的实例
	 */
	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
		return context -> {
			// 检查登录用户信息是不是UserDetails,排除掉没有用户参与的流程
			if (context.getPrincipal().getPrincipal() instanceof UserDetails) {
				UserDetails user = (UserDetails) context.getPrincipal().getPrincipal();
				// 获取申请的scopes
				Set<String> scopes = context.getAuthorizedScopes();
				// 获取用户的权限
				Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
				// 提取权限并转为字符串
				Set<String> authoritySet = Optional.ofNullable(authorities)
						.orElse(Collections.emptyList())
						.stream()
						// 获取权限字符串
						.map(GrantedAuthority::getAuthority)
						// 去重
						.collect(Collectors.toSet());

				// 合并scope与用户信息
				authoritySet.addAll(scopes);

				JwtClaimsSet.Builder claims = context.getClaims();
				// 将权限信息放入jwt的claims中（也可以生成一个以指定字符分割的字符串放入）
				claims.claim("authorities", authoritySet);
				// 放入其它自定内容
				// 角色、头像...
			}
		};
	}

	/**
	 * 自定义jwt解析器,设置解析出来的权限信息的前缀与在jwt中的key
	 *
	 * @return jwt解析器 JwtAuthenticationConverter
	 */
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		// 设置解析权限信息的前缀,设置为空是去掉前缀
		grantedAuthoritiesConverter.setAuthorityPrefix("");
		// 设置权限信息在jwt claims中的key
		grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	/**
	 * 将AuthenticationManager注入ioc中,其它需要使用地方可以直接从ioc中获取
	 *
	 * @param authenticationConfiguration 导出认证配置
	 * @return AuthenticationManager 认证管理器
	 */
	@Bean
	@SneakyThrows
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * 配置密码解析器,使用BCrypt的方式对密码进行加密和验证
	 *
	 * @return BCryptPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 配置基于db的oauth2的授权管理服务
	 *
	 * @param jdbcTemplate db数据源信息
	 * @param registeredClientRepository 上边注入的客户端repository
	 * @return JdbcOAuth2AuthorizationService
	 */
	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		// 基于db的oauth2认证服务,还有一个基于内存的服务实现InMemoryOAuth2AuthorizationService
		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * 配置基于db的授权确认管理服务
	 *
	 * @param jdbcTemplate db数据源信息
	 * @param registeredClientRepository 客户端repository
	 * @return JdbcOAuth2AuthorizationConsentService
	 */
	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		// 基于db的授权确认管理服务,还有一个基于内存的服务实现InMemoryOAuth2AuthorizationConsentService
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * 配置jwk源,使用非对称加密,公开用于检索匹配指定选择器的JWK的方法
	 *
	 * @return JWKSource
	 */
	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey =
				new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	/**
	 * 生成rsa密钥对,提供给jwk
	 *
	 * @return 密钥对
	 */
	private static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	/**
	 * 配置jwt解析器
	 *
	 * @param jwkSource jwk源
	 * @return JwtDecoder
	 */
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	/**
	 * 添加认证服务器配置,设置jwt签发者、默认端点请求地址等
	 *
	 * @return AuthorizationServerSettings
	 */
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				/*
				 * 设置token签发地址(http(s)://{ip}:{port}/context-path,
				 * http(s)://domain.com/context-path)
				 * 如果需要通过ip访问这里就是ip,如果是有域名映射就填域名,通过什么方式访问该服务这里就填什么
				 */
				.issuer("http://127.0.0.1:8080")
				.build();
	}

	/**
	 * 客户端配置,基于内存
	 */
	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate,
			PasswordEncoder passwordEncoder) {
		RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
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

		return new InMemoryRegisteredClientRepository(oidcClient);
	}
}