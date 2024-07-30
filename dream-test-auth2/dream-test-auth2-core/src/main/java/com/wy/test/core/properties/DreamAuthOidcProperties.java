package com.wy.test.core.properties;

import java.net.URI;

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
@ConfigurationProperties("dream.auth.oidc")
@Configuration
public class DreamAuthOidcProperties {

	private Metadata metadata = new Metadata();

	@Data
	public static class Metadata {

		private String issuer;

		private URI authorizationEndpoint;

		private URI tokenEndpoint;

		private URI userinfoEndpoint;
	}
}