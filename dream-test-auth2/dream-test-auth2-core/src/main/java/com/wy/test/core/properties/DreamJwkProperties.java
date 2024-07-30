package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * JWK配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.jwk")
@Configuration
public class DreamJwkProperties {

	private int expires = 86400;

	private String secret;

	private int refreshExpires;

	private String refreshSecret;

	private String issuer = "https://sso.dream.top/";
}