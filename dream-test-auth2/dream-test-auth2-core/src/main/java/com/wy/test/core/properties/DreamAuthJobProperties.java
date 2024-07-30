package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 定时任务配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.job")
@Configuration
public class DreamAuthJobProperties {

	private Cron cron = new Cron();

	@Data
	public static class Cron {

		private boolean enabled;

		private String schedule;
	}
}