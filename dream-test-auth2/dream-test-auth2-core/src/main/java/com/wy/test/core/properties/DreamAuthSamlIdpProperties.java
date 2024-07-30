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
@ConfigurationProperties("dream.auth.saml2.idp")
@Configuration
public class DreamAuthSamlIdpProperties {

	private String issuer;

	private Receiver receiver = new Receiver();

	@Data
	public static class Receiver {

		private boolean enabled = false;

		private String endpoint;
	}
}