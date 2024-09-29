package com.wy.test.core.enums;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证类型
 * 
 * @author 飞花梦影
 * @date 2024-07-30 21:34:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthType {

	/** 用户名密码 */
	NORMAL,

	/** 手机 */
	MOBILE,

	/** 短信 */
	SMS,

	/** 邮箱 */
	EMAIL,

	/** 基于时间 */
	TIME_BASED,

	TFA,

	TRUSTED,

	/** 基于计数器 */
	COUNTER_BASED,

	/**  */
	HOTP,

	/** RSA非对称加密 */
	RSA,

	/** 证书 */
	CERTIFICATE,

	/** usb证书 */
	USB_KEY;

	public static AuthType get(int code) {
		return Stream.of(values()).filter(t -> t.ordinal() == code).findFirst().orElse(null);
	}

	public static AuthType get(String code) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(code)).findFirst().orElse(null);
	}
}