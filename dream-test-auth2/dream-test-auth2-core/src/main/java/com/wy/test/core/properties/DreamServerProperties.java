package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
@Configuration
public class DreamServerProperties {

	private String baseDomain;

	private String domain;

	private String name;

	private String prefix;

	private String defaultUri;

	private String mgtUri;

	private String authzUri;

	private String frontendUri = "http://sso.dream.top:4200";

	private boolean provision = false;
}