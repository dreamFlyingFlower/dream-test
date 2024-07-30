package com.wy.test.autoconfigure;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wy.test.core.enums.StoreType;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.core.properties.DreamAuthStoreProperties;
import com.wy.test.oauth2.provider.client.ClientDetailsUserDetailsService;
import com.wy.test.oauth2.provider.client.JdbcClientDetailsService;
import com.wy.test.oauth2.provider.token.DefaultTokenServices;
import com.wy.test.oauth2.provider.token.TokenStore;
import com.wy.test.oauth2.provider.token.store.InMemoryTokenStore;
import com.wy.test.oauth2.provider.token.store.RedisTokenStore;

import lombok.extern.slf4j.Slf4j;

/**
 * like Oauth20AutoConfiguration for mgmt
 */
@AutoConfiguration
@Slf4j
public class Oauth20ClientAutoConfiguration implements InitializingBean {

	@Bean
	JdbcClientDetailsService oauth20JdbcClientDetailsService(DataSource dataSource,
			PasswordEncoder passwordReciprocal) {
		JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		// clientDetailsService.setPasswordEncoder(passwordReciprocal);
		log.debug("JdbcClientDetailsService inited.");
		return clientDetailsService;
	}

	/**
	 * TokenStore.
	 * 
	 * @param persistence int
	 * @return oauth20TokenStore
	 */
	@Bean
	TokenStore oauth20TokenStore(DreamAuthStoreProperties dreamAuthStoreProperties, JdbcTemplate jdbcTemplate,
			RedisConnectionFactory jedisConnectionFactory) {
		TokenStore tokenStore = null;
		if (StoreType.REDIS == dreamAuthStoreProperties.getStoreType()) {
			tokenStore = new RedisTokenStore(jedisConnectionFactory);
			log.debug("RedisTokenStore");
		} else {
			tokenStore = new InMemoryTokenStore();
			log.debug("InMemoryTokenStore");
		}

		return tokenStore;
	}

	/**
	 * clientDetailsUserDetailsService.
	 * 
	 * @return oauth20TokenServices
	 */
	@Bean
	DefaultTokenServices oauth20TokenServices(JdbcClientDetailsService oauth20JdbcClientDetailsService,
			TokenStore oauth20TokenStore) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setClientDetailsService(oauth20JdbcClientDetailsService);
		tokenServices.setTokenStore(oauth20TokenStore);
		tokenServices.setSupportRefreshToken(true);
		return tokenServices;
	}

	/**
	 * ProviderManager.
	 * 
	 * @return oauth20ClientAuthenticationManager
	 */
	@Bean
	ProviderManager oauth20ClientAuthenticationManager(JdbcClientDetailsService oauth20JdbcClientDetailsService,
			PasswordEncoder passwordReciprocal) {

		ClientDetailsUserDetailsService cientDetailsUserDetailsService =
				new ClientDetailsUserDetailsService(oauth20JdbcClientDetailsService);

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordReciprocal);
		daoAuthenticationProvider.setUserDetailsService(cientDetailsUserDetailsService);
		ProviderManager authenticationManager = new ProviderManager(daoAuthenticationProvider);
		log.debug("OAuth 2 Client Authentication Manager init.");
		return authenticationManager;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}
