package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * SAML配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.saml2")
@Configuration
public class DreamAuthSamlProperties {

	private int maxParserPoolSize;

	private Metadata metadata = new Metadata();

	private Replay replay = new Replay();

	@Data
	public static class Metadata {

		private boolean enabled = false;

		private String orgName;

		private String orgDisplayName;

		private String orgUrl;

		private String company;

		private String contactType;

		private String givenName;

		private String surName;

		private String emailAddress;

		private String phoneNumber;
	}

	@Data
	public static class Replay {

		private boolean enabled = false;

		private long cacheLifeInMillis;
	}
}