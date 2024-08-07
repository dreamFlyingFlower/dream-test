package com.wy.test.core.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * IP配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.ip")
@Configuration
public class DreamAuthIpProperties {

	private boolean enabled;

	private boolean white;

	private List<String> whiteUrls;
}