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
@ConfigurationProperties("dream.auth.saml2.issue")
@Configuration
public class DreamAuthSamlIssueProperties {

	private Instant instant = new Instant();

	@Data
	public static class Instant {

		private boolean enabled = false;

		private int checkClockSkewInSeconds;

		private int checkValidityTimeInSeconds;
	}
}