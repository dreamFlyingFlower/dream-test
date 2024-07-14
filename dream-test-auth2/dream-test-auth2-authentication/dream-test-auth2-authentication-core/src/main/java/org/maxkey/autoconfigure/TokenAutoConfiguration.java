/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.maxkey.autoconfigure;

import org.maxkey.authn.jwt.AuthRefreshTokenService;
import org.maxkey.authn.jwt.AuthTokenService;
import org.maxkey.authn.jwt.CongressService;
import org.maxkey.authn.jwt.InMemoryCongressService;
import org.maxkey.authn.jwt.RedisCongressService;
import org.maxkey.configuration.AuthJwkConfig;
import org.maxkey.constants.ConstsPersistence;
import org.maxkey.persistence.cache.MomentaryService;
import org.maxkey.persistence.redis.RedisConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import com.nimbusds.jose.JOSEException;


@AutoConfiguration
public class TokenAutoConfiguration  implements InitializingBean {
    private static final  Logger _logger = 
            LoggerFactory.getLogger(TokenAutoConfiguration.class);
    
    @Bean
    public AuthTokenService authTokenService(
    		AuthJwkConfig authJwkConfig,
    		RedisConnectionFactory redisConnFactory,
    		MomentaryService  momentaryService,
    		AuthRefreshTokenService refreshTokenService,
    		@Value("${maxkey.server.persistence}") int persistence) throws JOSEException {
    	CongressService congressService;
    	_logger.debug("cache persistence {}" , persistence);
    	if (persistence == ConstsPersistence.REDIS) {
    		congressService = new RedisCongressService(redisConnFactory);
    	}else {
    		congressService = new InMemoryCongressService();
    	}
    	
    	AuthTokenService authTokenService = 
    				new AuthTokenService(
    							authJwkConfig,
    							congressService,
    							momentaryService,
    							refreshTokenService
    						);
    	
    	return authTokenService;
    }
    
    @Bean
    public AuthRefreshTokenService refreshTokenService(AuthJwkConfig authJwkConfig) throws JOSEException {
    	return new AuthRefreshTokenService(authJwkConfig);
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        
    }
}
