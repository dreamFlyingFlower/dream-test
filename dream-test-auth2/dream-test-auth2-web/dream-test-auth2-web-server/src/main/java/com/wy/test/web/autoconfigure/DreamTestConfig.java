package com.wy.test.web.autoconfigure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wy.test.core.enums.StoreType;
import com.wy.test.core.persistence.redis.RedisConnectionFactory;
import com.wy.test.core.persistence.repository.LoginHistoryRepository;
import com.wy.test.core.persistence.repository.LoginRepository;
import com.wy.test.core.persistence.repository.PasswordPolicyValidator;
import com.wy.test.core.properties.DreamAuthLoginProperties;
import com.wy.test.core.properties.DreamAuthOtpProperties;
import com.wy.test.core.properties.DreamAuthStoreProperties;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.otp.password.onetimepwd.MailOtpAuthnService;
import com.wy.test.otp.password.onetimepwd.algorithm.OtpKeyUriFormat;
import com.wy.test.otp.password.onetimepwd.impl.MailOtpAuthn;
import com.wy.test.otp.password.onetimepwd.impl.TimeBasedOtpAuthn;
import com.wy.test.otp.password.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.persistence.service.LdapContextService;
import com.wy.test.persistence.service.UserService;
import com.wy.test.provider.authn.realm.jdbc.JdbcAuthenticationRealm;
import com.wy.test.provider.authn.realm.ldap.LdapAuthenticationRealmService;
import com.wy.test.provider.authn.support.kerberos.KerberosProxy;
import com.wy.test.provider.authn.support.kerberos.RemoteKerberosService;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@ComponentScan(basePackages = { "org.dream.authn", "org.dream.configuration", "org.dream.domain",
		"org.dream.domain.apps", "org.dream.domain.userinfo", "org.dream.api.v1.contorller", "org.dream.web.endpoint",
		"org.dream.web.contorller", "org.dream.web.interceptor",
		// single sign on protocol
		"org.dream.authz.endpoint", "org.dream.authz.desktop.endpoint", "org.dream.authz.exapi.endpoint",
		"org.dream.authz.formbased.endpoint", "org.dream.authz.ltpa.endpoint", "org.dream.authz.token.endpoint" })
@Slf4j
public class DreamTestConfig implements InitializingBean {

	@Bean
	OtpKeyUriFormat otpKeyUriFormat(DreamAuthOtpProperties dreamAuthOtpProperties) {
		OtpKeyUriFormat otpKeyUriFormat = new OtpKeyUriFormat(dreamAuthOtpProperties.getPolicy().getType(),
				dreamAuthOtpProperties.getPolicy().getIssuer(), dreamAuthOtpProperties.getPolicy().getDomain(),
				dreamAuthOtpProperties.getPolicy().getDigits(), dreamAuthOtpProperties.getPolicy().getPeriod());
		log.debug("OTP KeyUri Format " + otpKeyUriFormat);
		return otpKeyUriFormat;
	}

	// 可以在此实现其他的登陆认证方式，请实现AbstractAuthenticationRealm
	@Bean
	JdbcAuthenticationRealm authenticationRealm(PasswordEncoder passwordEncoder,
			PasswordPolicyValidator passwordPolicyValidator, LoginRepository loginService,
			LoginHistoryRepository loginHistoryService, UserService userService, JdbcTemplate jdbcTemplate,
			MailOtpAuthnService otpAuthnService, LdapContextService ldapContextService) {
		LdapAuthenticationRealmService ldapRealmService = new LdapAuthenticationRealmService(ldapContextService);
		JdbcAuthenticationRealm authenticationRealm =
				new JdbcAuthenticationRealm(passwordEncoder, passwordPolicyValidator, loginService, loginHistoryService,
						userService, jdbcTemplate, ldapRealmService);
		return authenticationRealm;
	}

	@Bean
	TimeBasedOtpAuthn timeBasedOtpAuthn(DreamAuthOtpProperties dreamAuthOtpProperties) {
		TimeBasedOtpAuthn tfaOtpAuthn = new TimeBasedOtpAuthn(dreamAuthOtpProperties.getPolicy().getDigits(),
				dreamAuthOtpProperties.getPolicy().getPeriod());
		log.debug("TimeBasedOtpAuthn inited.");
		return tfaOtpAuthn;
	}

	@Bean
	AbstractOtpAuthn tfaOtpAuthn(DreamAuthOtpProperties dreamAuthOtpProperties,
			DreamAuthStoreProperties dreamAuthStoreProperties, RedisConnectionFactory redisConnFactory) {
		AbstractOtpAuthn tfaOtpAuthn = new TimeBasedOtpAuthn(dreamAuthOtpProperties.getPolicy().getDigits(),
				dreamAuthOtpProperties.getPolicy().getPeriod());
		log.debug("TimeBasedOtpAuthn inited.");

		if (StoreType.REDIS == dreamAuthStoreProperties.getStoreType()) {
			RedisOtpTokenStore redisOptTokenStore = new RedisOtpTokenStore(redisConnFactory);
			tfaOtpAuthn.setOptTokenStore(redisOptTokenStore);
		}

		tfaOtpAuthn.initPropertys();
		return tfaOtpAuthn;
	}

	@Bean
	MailOtpAuthn mailOtpAuthn(MailProperties mailProperties,
			@Value("${spring.mail.properties.mailotp.message.subject}") String messageSubject,
			@Value("${spring.mail.properties.mailotp.message.template}") String messageTemplate,
			@Value("${spring.mail.properties.mailotp.message.validity}") int messageValidity,
			@Value("${spring.mail.properties.mailotp.message.type}") String messageType) {
		if (messageType != null && messageType.equalsIgnoreCase("html")) {
			Resource resource = new ClassPathResource("messages/email/forgotpassword.html");
			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
				messageTemplate = bufferedReader.lines().collect(Collectors.joining("\n"));
				bufferedReader.close();
			} catch (IOException e) {
				log.error("mailOtpAuthn IOException ", e);
			}
		}
		log.trace("messageTemplate \n" + messageTemplate);
		MailOtpAuthn mailOtpAuthn = new MailOtpAuthn();
		mailOtpAuthn.setSubject(messageSubject);
		mailOtpAuthn.setMessageTemplate(messageTemplate);
		mailOtpAuthn.setMailProperties(mailProperties);
		mailOtpAuthn.setInterval(messageValidity);
		log.debug("MailOtpAuthn inited.");
		return mailOtpAuthn;
	}

	@Bean
	RemoteKerberosService kerberosService(DreamAuthLoginProperties dreamAuthLoginProperties) {
		RemoteKerberosService kerberosService = new RemoteKerberosService();
		KerberosProxy kerberosProxy = new KerberosProxy();

		kerberosProxy.setCrypto(dreamAuthLoginProperties.getKerberos().getDefaultCrypto());
		kerberosProxy.setFullUserdomain(dreamAuthLoginProperties.getKerberos().getDefaultFullUserDomain());
		kerberosProxy.setUserdomain(dreamAuthLoginProperties.getKerberos().getDefaultUserDomain());
		kerberosProxy.setRedirectUri(dreamAuthLoginProperties.getKerberos().getDefaultRedirectUri());

		List<KerberosProxy> kerberosProxysList = new ArrayList<KerberosProxy>();
		kerberosProxysList.add(kerberosProxy);
		kerberosService.setKerberosProxys(kerberosProxysList);

		log.debug("RemoteKerberosService inited.");
		return kerberosService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}