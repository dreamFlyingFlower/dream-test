package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * ID配置
 *
 * @author 飞花梦影
 * @date 2024-07-18 22:05:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Configuration
@ConfigurationProperties("dream.auth.id")
public class DreamAuthIdProperties {

	private String strategy = "SnowFlake";

	private int datacenterId = 0;

	private int machineId = 0;
}