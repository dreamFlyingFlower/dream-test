package com.wy.test.web.core.handler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import dream.flying.flower.framework.mybatis.plus.handler.MybatisPlusHandler;
import dream.flying.flower.framework.mybatis.plus.properties.DreamMybatisPlusProperties;

/**
 * 自动给INSERT,UPDATE添加值
 *
 * @author 飞花梦影
 * @date 2024-10-02 16:28:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@EnableConfigurationProperties(DreamMybatisPlusProperties.class)
@Component
public class DefaultMybatisHandler extends MybatisPlusHandler {

	public DefaultMybatisHandler(DreamMybatisPlusProperties myBatisPlusProperties) {
		super(myBatisPlusProperties);
	}
}