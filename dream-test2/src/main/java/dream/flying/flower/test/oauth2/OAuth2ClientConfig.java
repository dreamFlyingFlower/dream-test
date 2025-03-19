//package dream.flying.flower.test.oauth2;
//
//import java.net.URI;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.RequestEntity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.util.UriComponentsBuilder;
//
///**
// * 调用第三方OAuth2接口
// *
// * @author 飞花梦影
// * @date 2024-09-18 20:20:47
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@Configuration
//public class OAuth2ClientConfig {
//
//	/**
//	 * 配置内存客户端验证,默认就是内存
//	 * 
//	 * @return ClientRegistrationRepository
//	 */
//	@Bean
//	public ClientRegistrationRepository clientRegistrationRepository() {
//		return new InMemoryClientRegistrationRepository(this.giteeClientRegistration());
//	}
//
//	// 配置giee的授权登录信息
//	private ClientRegistration giteeClientRegistration() {
//		return ClientRegistration
//				// 第三方认证服务器唯一标识,自定义,不可重复
//				.withRegistrationId("gitee")
//				// 第三方认证服务器发放给本系统的客户端ID和客户端密钥
//				.clientId("clientId")
//				.clientSecret("clientSecret")
//				// 第三方认证服务器指定本系统的认证方式
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				// 第三方认证服务器指定本系统的授权模式
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				// 服务提供者回调本系统时的API地址
//				.redirectUri("http://localhost:8080/login/oauth2/code/gitee")
//				// 授权范围,即创建应用时勾选的权限名
//				.scope("user_info")
//				// 第三方认证服务器授权地址
//				.authorizationUri("https://gitee.com/oauth/authorize")
//				// 获取第三方认证服务器token地址
//				.tokenUri("https://gitee.com/oauth/token")
//				// 获取第三方认证服务器用户信息地址
//				.userInfoUri("https://gitee.com/api/v5/user")
//				.userNameAttributeName("name")
//				.build();
//	}
//
//	private OAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
//		DefaultOAuth2UserService userService = new DefaultOAuth2UserService();
//		// 注入自定义的requestEntityConverter
//		userService.setRequestEntityConverter(new WechatOAuth2UserRequestEntityConverter());
//		return userService;
//	}
//
//	private static class WechatOAuth2UserRequestEntityConverter
//			implements Converter<OAuth2UserRequest, RequestEntity<?>> {
//
//		@Override
//		public RequestEntity<?> convert(OAuth2UserRequest userRequest) {
//			ClientRegistration clientRegistration = userRequest.getClientRegistration();
//			URI uri = UriComponentsBuilder
//					.fromUriString(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
//					.queryParam(OAuth2ParameterNames.ACCESS_TOKEN, userRequest.getAccessToken().getTokenValue())
//					.build()
//					.toUri();
//			return new RequestEntity<>(HttpMethod.GET, uri);
//		}
//	}
//
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity
//				// 自定义oauth2,配置OAuth2 Client和OAuth2 Server交互,启用SSO
//				.oauth2Login(oauth2 -> oauth2
//						// 登录地址
//						// .loginPage(null)
//						// 自定义登录成功方法
//						// .successHandler(null)
//						// 获取用户信息,自定义service,配置OAuth2UserService的实例
//						.userInfoEndpoint(userInfo -> userInfo.userService(userService())));
//
//		// 自定义oauth2资源服务器
//		// httpSecurity.oauth2ResourceServer((oauth2) ->
//		// oauth2.jwt(Customizer.withDefaults()));
//
//		return httpSecurity.build();
//	}
//}