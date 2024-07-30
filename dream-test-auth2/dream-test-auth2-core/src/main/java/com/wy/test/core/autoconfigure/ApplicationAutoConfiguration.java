package com.wy.test.core.autoconfigure;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import com.nimbusds.jose.JOSEException;
import com.wy.test.core.enums.StoreType;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.password.SM3PasswordEncoder;
import com.wy.test.core.persistence.cache.InMemoryMomentaryService;
import com.wy.test.core.persistence.cache.MomentaryService;
import com.wy.test.core.persistence.cache.RedisMomentaryService;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.core.persistence.repository.InstitutionsRepository;
import com.wy.test.core.persistence.repository.LocalizationRepository;
import com.wy.test.core.properties.DreamAuthCryptoProperties;
import com.wy.test.core.properties.DreamAuthIdProperties;
import com.wy.test.core.properties.DreamAuthStoreProperties;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.framework.web.crypto.keystore.KeyStoreLoader;
import dream.flying.flower.generator.GeneratorStrategyContext;
import dream.flying.flower.generator.SnowFlakeGenerator;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@AutoConfiguration
@Slf4j
public class ApplicationAutoConfiguration implements InitializingBean {

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
	PasswordEncoder passwordEncoder(DreamAuthCryptoProperties dreamAuthCryptoProperties) {
		Map<String, PasswordEncoder> encoders = new HashMap<String, PasswordEncoder>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("sha256", new StandardPasswordEncoder());

		encoders.put("sm3", new SM3PasswordEncoder());

		encoders.put("ldap", new LdapShaPasswordEncoder());

		// idForEncode is default for encoder
		PasswordEncoder passwordEncoder =
				new DelegatingPasswordEncoder(dreamAuthCryptoProperties.getPasswordType(), encoders);

		if (log.isTraceEnabled()) {
			log.trace("Password Encoders :");
			for (String key : encoders.keySet()) {
				log.trace("{}= {}", String.format("%-10s", key), encoders.get(key).getClass().getName());
			}
		}
		log.debug("{} is default encoder", dreamAuthCryptoProperties.getPasswordType());
		return passwordEncoder;
	}

	/**
	 * keyStoreLoader .
	 * 
	 * @return
	 */
	@Bean
	KeyStoreLoader keyStoreLoader(@Value("${dream.saml.v20.idp.issuing.entity.id}") String entityName,
			@Value("${dream.saml.v20.idp.keystore.password}") String keystorePassword,
			@Value("${dream.saml.v20.idp.keystore}") Resource keystoreFile) {
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
	KeyStoreLoader spKeyStoreLoader(@Value("${dream.saml.v20.sp.issuing.entity.id}") String entityName,
			@Value("${dream.saml.v20.sp.keystore.password}") String keystorePassword,
			@Value("${dream.saml.v20.sp.keystore}") Resource keystoreFile) {
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
	String spIssuingEntityName(@Value("${dream.saml.v20.sp.issuing.entity.id}") String spIssuingEntityName) {
		return spIssuingEntityName;
	}

	/**
	 * Id Generator .
	 * 
	 * @return
	 */
	@Bean
	GeneratorStrategyContext idGenerator(DreamAuthIdProperties dreamAuthIdProperties) {
		GeneratorStrategyContext idGenerator = new GeneratorStrategyContext();
		idGenerator.setStrategy(dreamAuthIdProperties.getStrategy());
		SnowFlakeGenerator SnowFlakeId =
				new SnowFlakeGenerator(dreamAuthIdProperties.getDatacenterId(), dreamAuthIdProperties.getMachineId());
		idGenerator.setSnowFlakeGenerator(SnowFlakeId);
		WebContext.idGenerator = idGenerator;
		return idGenerator;
	}

	@Bean
	MomentaryService momentaryService(RedisConnectionFactory redisConnFactory,
			DreamAuthStoreProperties dreamAuthRedisProperties) throws JOSEException {
		MomentaryService momentaryService;
		if (dreamAuthRedisProperties.getStoreType() == StoreType.REDIS) {
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