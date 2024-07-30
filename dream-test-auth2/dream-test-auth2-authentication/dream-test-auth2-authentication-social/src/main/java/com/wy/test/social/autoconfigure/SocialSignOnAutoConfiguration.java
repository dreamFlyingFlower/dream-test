package com.wy.test.social.autoconfigure;

import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.core.entity.SocialsProvider;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.social.authn.support.socialsignon.service.JdbcSocialsAssociateService;
import com.wy.test.social.authn.support.socialsignon.service.SocialSignOnProviderService;
import com.wy.test.social.authn.support.socialsignon.token.RedisTokenStore;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@ComponentScan(basePackages = { "org.wy.test.social.authn.support.socialsignon" })
@Slf4j
public class SocialSignOnAutoConfiguration implements InitializingBean {

	@Bean(name = "socialSignOnProviderService")
	@ConditionalOnClass(SocialsProvider.class)
	SocialSignOnProviderService socialSignOnProviderService(JdbcTemplate jdbcTemplate,
			RedisConnectionFactory redisConnFactory) throws IOException {
		SocialSignOnProviderService socialSignOnProviderService = new SocialSignOnProviderService(jdbcTemplate);
		// load default Social Providers from database
		socialSignOnProviderService.loadSocials("1");

		RedisTokenStore redisTokenStore = new RedisTokenStore();
		socialSignOnProviderService.setRedisTokenStore(redisTokenStore);

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