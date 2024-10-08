package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.wy.test.core.enums.StoreType;

import lombok.Data;

/**
 * 服务配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.server")
public class DreamAuthServerProperties {

	private String scheme;

	private String baseDomain;

	private String domain;

	private String subDomain;

	private String name;

	private String uri;

	private String defaultUri;

	private String mgtUri;

	private String authzUri;

	/**
	 * CAS未登录时跳转地址
	 */
	private String frontendUri = "http://sso.dream.top:4200";

	private Integer frontendPort = 4200;

	private StoreType storeType = StoreType.INMEMORY;

	private boolean provision = false;
}