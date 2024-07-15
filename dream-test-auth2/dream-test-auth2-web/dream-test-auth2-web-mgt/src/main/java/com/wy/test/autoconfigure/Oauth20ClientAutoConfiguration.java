package com.wy.test.autoconfigure;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wy.test.authz.oauth2.provider.client.ClientDetailsUserDetailsService;
import com.wy.test.authz.oauth2.provider.client.JdbcClientDetailsService;
import com.wy.test.authz.oauth2.provider.token.DefaultTokenServices;
import com.wy.test.authz.oauth2.provider.token.TokenStore;
import com.wy.test.authz.oauth2.provider.token.store.InMemoryTokenStore;
import com.wy.test.authz.oauth2.provider.token.store.RedisTokenStore;
import com.wy.test.persistence.redis.RedisConnectionFactory;

/**
 * like Oauth20AutoConfiguration for mgmt
 */
@AutoConfiguration
public class Oauth20ClientAutoConfiguration  implements InitializingBean {
    private static final  Logger _logger = LoggerFactory.getLogger(Oauth20ClientAutoConfiguration.class);
    
    @Bean
    public JdbcClientDetailsService oauth20JdbcClientDetailsService(
                DataSource dataSource,PasswordEncoder passwordReciprocal) {
	    JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
	    //clientDetailsService.setPasswordEncoder(passwordReciprocal);
	    _logger.debug("JdbcClientDetailsService inited.");
        return clientDetailsService;
    }
	
    /**
     * TokenStore. 
     * @param persistence int
     * @return oauth20TokenStore
     */
    @Bean
    public TokenStore oauth20TokenStore(
            @Value("${maxkey.server.persistence}") int persistence,
            JdbcTemplate jdbcTemplate,
            RedisConnectionFactory jedisConnectionFactory) {
        TokenStore tokenStore = null;
        if (persistence == 2) {
            tokenStore = new RedisTokenStore(jedisConnectionFactory);
            _logger.debug("RedisTokenStore");
        }else {
            tokenStore = new InMemoryTokenStore();
            _logger.debug("InMemoryTokenStore"); 
        }
        
        return tokenStore;
    }
    
    /**
     * clientDetailsUserDetailsService. 
     * @return oauth20TokenServices
     */
    @Bean
    public DefaultTokenServices oauth20TokenServices(
            JdbcClientDetailsService oauth20JdbcClientDetailsService,
            TokenStore oauth20TokenStore) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setClientDetailsService(oauth20JdbcClientDetailsService);
        tokenServices.setTokenStore(oauth20TokenStore);
        tokenServices.setSupportRefreshToken(true);
        return tokenServices;
    }
    
    /**
     * ProviderManager. 
     * @return oauth20ClientAuthenticationManager
     */
    @Bean
    public ProviderManager oauth20ClientAuthenticationManager(
            JdbcClientDetailsService oauth20JdbcClientDetailsService,
            PasswordEncoder passwordReciprocal
            ) {
        
        ClientDetailsUserDetailsService cientDetailsUserDetailsService = 
                new ClientDetailsUserDetailsService(oauth20JdbcClientDetailsService);
        
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordReciprocal);
        daoAuthenticationProvider.setUserDetailsService(cientDetailsUserDetailsService);
        ProviderManager authenticationManager = new ProviderManager(daoAuthenticationProvider);
        _logger.debug("OAuth 2 Client Authentication Manager init.");
        return authenticationManager;
    }
  
    @Override
    public void afterPropertiesSet() throws Exception {
        
    }

}
