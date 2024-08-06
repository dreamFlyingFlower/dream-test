package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 登录配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.login")
@Configuration
public class DreamAuthLoginProperties {

	/**
	 * 是否启动记住我
	 */
	private boolean rememberMe;

	private Long rememberMeValidity;

	private boolean wsFederation;

	private boolean basicEnabled = false;

	private Captcha captcha = new Captcha();

	private Mfa mfa = new Mfa();

	private HttpHeader httpHeader = new HttpHeader();

	private Kerberos kerberos = new Kerberos();

	private Jwt jwt = new Jwt();

	@Data
	public static class Captcha {

		private boolean enabled;

		private String type;
	}

	@Data
	public static class Mfa {

		private boolean enabled;

		private String type;
	}

	@Data
	public static class HttpHeader {

		private boolean enabled = false;

		private String headerName = "iv-user";
	}

	@Data
	public static class Kerberos {

		private boolean enabled = false;

		private String defaultUserDomain;

		private String defaultFullUserDomain;

		private String defaultCrypto;

		private String defaultRedirectUri;
	}

	@Data
	public static class Jwt {

		private boolean enabled = false;

		private String issuer;
	}
}