package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * OTP配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.otp")
@Configuration
public class DreamAuthOtpProperties {

	private boolean enabled = false;

	private Policy policy = new Policy();

	private Keyuri keyuri = new Keyuri();

	@Data
	public static class Policy {

		private boolean enabled = false;

		private String type = "totp";

		private String domain = "dream.top";

		private String issuer = "dream";

		private int digits = 6;

		private int period = 30;
	}

	@Data
	public static class Keyuri {

		private boolean enabled = false;

		private String type = "totp";

		private String domain = "dream.top";

		private String issuer = "dream";

		private int digits = 6;

		private int period = 30;
	}
}