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
@ConfigurationProperties("dream.auth.crypto")
@Configuration
public class DreamAuthCryptoProperties {

	/**
	 * 加密类型,见com.wy.test.core.autoconfigure.ApplicationAutoConfiguration.passwordEncoder(String)
	 */
	private String passwordType = "bcrypt";
}