package com.wy.test.core.properties;

import java.util.List;

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

	private String defaultUri;

	/**
	 * 是否启动记住我
	 */
	private boolean rememberMe;

	private Long rememberMeValidity;

	private Captcha captcha = new Captcha();

	private Mfa mfa = new Mfa();

	private HttpHeader httpHeader = new HttpHeader();

	private Basic basic = new Basic();

	private SocialSign socialSign = new SocialSign();

	private Kerberos kerberos = new Kerberos();

	private WsFederation wsFederation = new WsFederation();

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

		/**
		 * iv-user is for IBM Security Access Manager
		 */
		private String headerName = "iv-user";
	}

	@Data
	public static class Basic {

		private boolean enabled = false;
	}

	@Data
	public static class SocialSign {

		private boolean enabled = true;

		private List<String> providers;
	}

	@Data
	public static class Kerberos {

		private boolean enabled = true;

		private String defaultUserDomain;

		private String defaultFullUserDomain;

		private String defaultCrypto;

		private String defaultRedirectUri;
	}

	/**
	 * <pre>
	 * identifier: the identifer for the ADFS server
	 * url: the login url for ADFS
	 * principal: the name of the attribute/assertion returned by ADFS that contains the principal's username.
	 * relyingParty: the identifier of the CAS Server as it has been configured in ADFS.
	 * tolerance: (optional) the amount of drift to allow when validating the timestamp on the token. Default: 10000 (ms)
	 * attributeMutator: (optional) a class (defined by you) that can modify the attributes/assertions returned by the ADFS server
	 * signingCertificate: ADFS's signing certificate used to validate the token/assertions issued by ADFS.
	 * </pre>
	 *
	 * @author 飞花梦影
	 * @date 2024-08-07 17:56:03
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	@Data
	public static class WsFederation {

		private boolean enabled = true;

		private String identifier = "http://adfs.dream.top/adfs/services/trust";

		private String url = "https://adfs.dream.top/adfs/ls/";

		private String principal = "upn";

		private String relyingParty = "urn:federation:connsec";

		private String signingCertificate = "adfs-signing.crt";

		private String tolerance = "10000";

		private String upnSuffix = "dream.org";

		private String logoutUrl = "https://adfs.dream.top/adfs/ls/?wa=wsignout1.0";
	}

	@Data
	public static class Jwt {

		private boolean enabled = false;

		private String issuer;
	}
}