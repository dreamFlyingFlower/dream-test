package com.wy.test.social.autoconfigure;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wy.test.entity.SocialsProvider;
import com.wy.test.persistence.redis.RedisConnectionFactory;
import com.wy.test.social.authn.support.socialsignon.service.JdbcSocialsAssociateService;
import com.wy.test.social.authn.support.socialsignon.service.SocialSignOnProviderService;
import com.wy.test.social.authn.support.socialsignon.token.RedisTokenStore;

@AutoConfiguration
@ComponentScan(basePackages = {
        "org.maxkey.authn.support.socialsignon"
})
public class SocialSignOnAutoConfiguration implements InitializingBean {
    private static final  Logger _logger = LoggerFactory.getLogger(SocialSignOnAutoConfiguration.class);
    
    @Bean(name = "socialSignOnProviderService")
    @ConditionalOnClass(SocialsProvider.class)
    public SocialSignOnProviderService socialSignOnProviderService(
            @Value("${maxkey.server.persistence}") int persistence,
            JdbcTemplate jdbcTemplate,
            RedisConnectionFactory redisConnFactory
                    ) throws IOException {
        SocialSignOnProviderService socialSignOnProviderService = new SocialSignOnProviderService(jdbcTemplate);
        //load default Social Providers from database
        socialSignOnProviderService.loadSocials("1");

        RedisTokenStore redisTokenStore = new RedisTokenStore();
        socialSignOnProviderService.setRedisTokenStore(redisTokenStore);

        _logger.debug("SocialSignOnProviderService inited.");
        return socialSignOnProviderService;
    }
    
    @Bean(name = "socialsAssociateService")
    public JdbcSocialsAssociateService socialsAssociateService(
                JdbcTemplate jdbcTemplate) {
        JdbcSocialsAssociateService socialsAssociateService = new JdbcSocialsAssociateService(jdbcTemplate);
        _logger.debug("JdbcSocialsAssociateService inited.");
        return socialsAssociateService;
    }
   

    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
}
