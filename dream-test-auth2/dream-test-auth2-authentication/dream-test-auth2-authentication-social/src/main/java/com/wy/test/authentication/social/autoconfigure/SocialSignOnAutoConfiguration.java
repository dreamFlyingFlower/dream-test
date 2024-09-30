package com.wy.test.authentication.social.autoconfigure;

import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.authentication.social.sso.service.JdbcSocialsAssociateService;
import com.wy.test.authentication.social.sso.service.SocialSignOnProviderService;
import com.wy.test.authentication.social.sso.token.RedisTokenStore;
import com.wy.test.core.entity.SocialProviderEntity;
import com.wy.test.core.redis.RedisConnectionFactory;
import com.wy.test.persistence.mapper.SocialProviderMapper;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@ComponentScan(basePackages = { "org.wy.test.social.authn.support.socialsignon" })
@Slf4j
public class SocialSignOnAutoConfiguration implements InitializingBean {

	@Bean(name = "socialSignOnProviderService")
	@ConditionalOnClass(SocialProviderEntity.class)
	SocialSignOnProviderService socialSignOnProviderService(SocialProviderMapper socialProviderMapper,
			RedisConnectionFactory redisConnFactory) throws IOException {
		RedisTokenStore redisTokenStore = new RedisTokenStore();
		SocialSignOnProviderService socialSignOnProviderService =
				new SocialSignOnProviderService(socialProviderMapper, redisTokenStore);
		// load default Social Providers from database
		socialSignOnProviderService.loadSocials("1");
		log.debug("SocialSignOnProviderService inited.");
		return socialSignOnProviderService;
	}

	@Bean(name = "socialsAssociateService")
	JdbcSocialsAssociateService socialsAssociateService(JdbcTemplate jdbcTemplate) {
		JdbcSocialsAssociateService socialsAssociateService = new JdbcSocialsAssociateService(jdbcTemplate);
		log.debug("JdbcSocialsAssociateService inited.");
		return socialsAssociateService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}