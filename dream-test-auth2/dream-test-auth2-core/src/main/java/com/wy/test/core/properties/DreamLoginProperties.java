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
public class DreamLoginProperties {

	private boolean captcha;

	private boolean mfa;

	private boolean kerberos;

	private boolean remeberMe;

	private boolean wsFederation;
}