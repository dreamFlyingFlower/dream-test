package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * OTP短信配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.otp.sms")
@Configuration
public class DreamAuthOtpSmsProperties {

	private boolean enabled = false;

	private String className = "com.wy.test.sms.password.sms.impl.SmsOtpAuthnYunxin";

	private SmsProperties aliyun = new SmsProperties();

	private SmsProperties yunxin = new SmsProperties();

	private SmsProperties tencentcloud = new SmsProperties();

	@Data
	public static class SmsProperties {

		private boolean enabled = false;

		private String accesskeyid;

		private String accesssecret;

		private String templatecode;

		private String signname;

		private String appkey;

		private String appsecret;

		private String templateid;

		private String secretid;

		private String secretkey;

		private String smssdkappid;

		private String sign;
	}
}