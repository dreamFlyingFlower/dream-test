package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.wy.test.core.enums.StoreType;

import lombok.Data;

/**
 * 存储配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.redis")
@Configuration
public class DreamAuthStoreProperties {

	/**
	 * 存储类型
	 */
	private StoreType storeType = StoreType.INMEMORY;
}