package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Mybatis相关配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.mybatis")
@Configuration
public class DreamAuthMybatisAppProperties {

	private String dialect = "mysql";

	private Integer tableColumnSnowflakeDatacenterId = 1;

	private Integer tableColumnSnowflakeMachineId = 1;

	private Boolean tableColumnEscape = false;
}