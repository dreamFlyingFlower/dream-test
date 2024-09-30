package com.wy.test.authentication.provider.provider;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;

import com.wy.test.authentication.core.entity.LoginCredential;
import com.wy.test.core.enums.AuthType;

/**
 * 登录认证抽象工厂,管理所有的认证提供者
 *
 * @author 飞花梦影
 * @date 2024-09-30 10:43:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class AuthenticationProviderFactory extends AbstractAuthenticationProvider {

	private static ConcurrentHashMap<String, AbstractAuthenticationProvider> providers =
			new ConcurrentHashMap<String, AbstractAuthenticationProvider>();

	@Override
	public Authentication authenticate(LoginCredential authentication) {
		if (authentication.getAuthType().equalsIgnoreCase("trusted")) {
			// risk remove
			return null;
		}
		AbstractAuthenticationProvider provider =
				providers.get(authentication.getAuthType().toLowerCase() + PROVIDER_SUFFIX);

		return provider == null ? null : provider.doAuthenticate(authentication);
	}

	@Override
	public Authentication authenticate(LoginCredential authentication, boolean trusted) {
		AbstractAuthenticationProvider provider =
				providers.get(AuthType.TRUSTED.name().toLowerCase() + PROVIDER_SUFFIX);
		return provider.doAuthenticate(authentication);
	}

	public void addAuthenticationProvider(AbstractAuthenticationProvider provider) {
		providers.put(provider.getProviderName(), provider);
	}

	@Override
	public String getProviderName() {
		return "AuthenticationProviderFactory";
	}

	@Override
	public Authentication doAuthenticate(LoginCredential authentication) {
		// AuthenticationProvider Factory do nothing
		return null;
	}
}