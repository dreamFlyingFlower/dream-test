package com.wy.test.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * APP配置
 *
 * @author 飞花梦影
 * @date 2024-07-30 09:14:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.auth.captcha")
@Configuration
public class DreamAuthCaptchaProperties {

	private Integer imageWidth = 120;

	private Integer imageHeight = 40;

	private Boolean border = false;

	// private String obscurificatorImpl=com.google.code.kaptcha.impl.ShadowGimpy;
	private String obscurificatorImpl = "com.google.code.kaptcha.impl.Ripple";

	private Integer textproducerFontSize = 30;

	private String textproducerCharString = "0123456789";

	private Integer textproducerCharLength = 4;

	private Integer textproducerCharSpace = 6;

	// private String noiseImpl=com.google.code.kaptcha.impl.NoNoise;
	// private String noiseImpl=com.google.code.kaptcha.impl.DefaultNoise;
	private String noiseImpl = "com.google.code.kaptcha.impl.LightNoise";

	// private String noiseColor="white";
	private String wordImpl = "com.google.code.kaptcha.text.impl.RandomColorWordRenderer";

	private String textproducerImpl = "com.google.code.kaptcha.impl.UniqueTextCreator";
}