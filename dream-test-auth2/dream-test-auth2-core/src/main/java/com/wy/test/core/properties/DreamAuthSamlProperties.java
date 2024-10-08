package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.Data;

/**
 * SAML配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.saml")
public class DreamAuthSamlProperties {

	private int maxParserPoolSize = 2;

	private Assertion assertion = new Assertion();

	private Replay replay = new Replay();

	private Issue issue = new Issue();

	private Idp idp = new Idp();

	private Sp sp = new Sp();

	private Metadata metadata = new Metadata();

	@Data
	public static class Assertion {

		private boolean enabled = false;

		private long validityTimeInSeconds = 90;
	}

	@Data
	public static class Replay {

		private boolean enabled = false;

		private long cacheLifeInMillis = 14400000;
	}

	@Data
	public static class Issue {

		private boolean enabled = false;

		private Integer instantCheckClockSkewInSeconds = 90;

		private Integer instantCheckValidityTimeInSeconds = 300;
	}

	@Data
	public static class Idp {

		private boolean enabled = false;

		/** JKS文件存储路径,若写在配置文件,需要添加classpath:前缀 */
		private Resource keystore = new ClassPathResource("config/samlServerKeystore.jks");

		private String alias = "AuthServerKey";

		private String keystorePassword = "DreamFlyingFlower";

		private String keystorePrivateKeyPassword = "DreamFlyingFlower";

		private String issuingEntityId = "dream.top";

		private String issuer = "https://sso.dream.top/dream/saml";

		private String receiverEndpoint = "https://sso.dream.top/";
	}

	@Data
	public static class Sp {

		private boolean enabled = false;

		/** JKS文件存储路径,若写在配置文件,需要添加classpath:前缀 */
		private Resource keystore = new ClassPathResource("config/samlClientKeystore.jks");

		private String alias = "AuthClientKey";

		private String keystorePassword = "DreamFlyingFlower";

		private String keystorePrivateKeyPassword = "DreamFlyingFlower";

		private String issuingEntityId = "client.dream.org";
	}

	@Data
	public static class Metadata {

		private boolean enabled = false;

		private String orgName = "dream";

		private String orgDisplayName = "dream";

		private String orgUrl = "https://github.com/shimingxy/dream";

		private String company = "dream";

		private String contactType = "technical";

		private String givenName = "dream";

		private String surName = "dream";

		private String email = "shimingxy@163.com";

		private String phoneNumber = "4008981111";
	}
}