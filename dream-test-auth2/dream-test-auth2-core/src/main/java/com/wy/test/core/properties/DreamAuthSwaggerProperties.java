package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Swagger配置
 *
 * @author 飞花梦影
 * @date 2024-07-18 22:05:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Configuration
@ConfigurationProperties("dream.auth.swagger")
public class DreamAuthSwaggerProperties {

	private boolean enabled;

	private String title;

	private String description;

	private String version;
}