package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Social登录配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.social-sign")
public class DreamAuthSocialSignProperties {

	private Provider weibo = new Provider();

	private Provider google = new Provider();

	private Provider qq = new Provider();

	private Provider dingTalk = new Provider();

	private Provider microsoft = new Provider();

	private Provider facebook = new Provider();

	@Data
	public static class Provider {

		protected boolean enabled;

		protected String provider;

		protected String name;

		protected String icon;

		protected String clientId;

		protected String clientSecret;

		protected String accountId;

		protected Integer sortOrder;
	}
}