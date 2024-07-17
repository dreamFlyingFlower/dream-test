package com.wy.test.autoconfigure;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import com.nimbusds.jose.JOSEException;
import com.wy.test.constants.ConstsPersistence;
import com.wy.test.crypto.keystore.KeyStoreLoader;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.crypto.password.SM3PasswordEncoder;
import com.wy.test.crypto.password.StandardPasswordEncoder;
import com.wy.test.persistence.cache.InMemoryMomentaryService;
import com.wy.test.persistence.cache.MomentaryService;
import com.wy.test.persistence.cache.RedisMomentaryService;
import com.wy.test.persistence.redis.RedisConnectionFactory;
import com.wy.test.persistence.repository.InstitutionsRepository;
import com.wy.test.persistence.repository.LocalizationRepository;
import com.wy.test.util.IdGenerator;
import com.wy.test.util.SnowFlakeId;
import com.wy.test.web.WebContext;

@AutoConfiguration
public class ApplicationAutoConfiguration implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(ApplicationAutoConfiguration.class);

	@Bean
	 PasswordReciprocal passwordReciprocal() {
		return new PasswordReciprocal();
	}

	@Bean
	 DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	 InstitutionsRepository institutionsRepository(JdbcTemplate jdbcTemplate) {
		return new InstitutionsRepository(jdbcTemplate);
	}

	@Bean
	 LocalizationRepository localizationRepository(JdbcTemplate jdbcTemplate,
			InstitutionsRepository institutionsRepository) {
		return new LocalizationRepository(jdbcTemplate, institutionsRepository);
	}

	/**
	 * Authentication Password Encoder .
	 * 
	 * 参照{@link PasswordEncoderFactories}
	 * 
	 * @return  
	 */
	@Bean
	 PasswordEncoder passwordEncoder(@Value("${maxkey.crypto.password.encoder:bcrypt}") String idForEncode) {
		Map<String, PasswordEncoder> encoders = new HashMap<String, PasswordEncoder>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("sha1", new StandardPasswordEncoder("SHA-1", ""));
		encoders.put("sha256", new StandardPasswordEncoder());
		encoders.put("sha384", new StandardPasswordEncoder("SHA-384", ""));
		encoders.put("sha512", new StandardPasswordEncoder("SHA-512", ""));

		encoders.put("sm3", new SM3PasswordEncoder());

		encoders.put("ldap", new LdapShaPasswordEncoder());

		// idForEncode is default for encoder
		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);

		if (_logger.isTraceEnabled()) {
			_logger.trace("Password Encoders :");
			for (String key : encoders.keySet()) {
				_logger.trace("{}= {}", String.format("%-10s", key), encoders.get(key).getClass().getName());
			}
		}
		_logger.debug("{} is default encoder", idForEncode);
		return passwordEncoder;
	}

	/**
	 * keyStoreLoader .
	 * 
	 * @return
	 */
	@Bean
	 KeyStoreLoader keyStoreLoader(@Value("${maxkey.saml.v20.idp.issuing.entity.id}") String entityName,
			@Value("${maxkey.saml.v20.idp.keystore.password}") String keystorePassword,
			@Value("${maxkey.saml.v20.idp.keystore}") Resource keystoreFile) {
		KeyStoreLoader keyStoreLoader = new KeyStoreLoader();
		keyStoreLoader.setEntityName(entityName);
		keyStoreLoader.setKeystorePassword(keystorePassword);
		keyStoreLoader.setKeystoreFile(keystoreFile);
		return keyStoreLoader;
	}

	/**
	 * spKeyStoreLoader .
	 * 
	 * @return
	 */
	@Bean
	 KeyStoreLoader spKeyStoreLoader(@Value("${maxkey.saml.v20.sp.issuing.entity.id}") String entityName,
			@Value("${maxkey.saml.v20.sp.keystore.password}") String keystorePassword,
			@Value("${maxkey.saml.v20.sp.keystore}") Resource keystoreFile) {
		KeyStoreLoader keyStoreLoader = new KeyStoreLoader();
		keyStoreLoader.setEntityName(entityName);
		keyStoreLoader.setKeystorePassword(keystorePassword);
		keyStoreLoader.setKeystoreFile(keystoreFile);
		return keyStoreLoader;
	}

	/**
	 * spKeyStoreLoader .
	 * 
	 * @return
	 */
	@Bean
	 String spIssuingEntityName(@Value("${maxkey.saml.v20.sp.issuing.entity.id}") String spIssuingEntityName) {
		return spIssuingEntityName;
	}

	/**
	 * Id Generator .
	 * 
	 * @return
	 */
	@Bean
	 IdGenerator idGenerator(@Value("${maxkey.id.strategy:SnowFlake}") String strategy,
			@Value("${maxkey.id.datacenterId:0}") int datacenterId, @Value("${maxkey.id.machineId:0}") int machineId) {
		IdGenerator idGenerator = new IdGenerator(strategy);
		SnowFlakeId SnowFlakeId = new SnowFlakeId(datacenterId, machineId);
		idGenerator.setSnowFlakeId(SnowFlakeId);
		WebContext.idGenerator = idGenerator;
		return idGenerator;
	}

	@Bean
	MomentaryService momentaryService(RedisConnectionFactory redisConnFactory,
			@Value("${maxkey.server.persistence}") int persistence) throws JOSEException {
		MomentaryService momentaryService;
		if (persistence == ConstsPersistence.REDIS) {
			momentaryService = new RedisMomentaryService(redisConnFactory);
		} else {
			momentaryService = new InMemoryMomentaryService();
		}

		return momentaryService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
