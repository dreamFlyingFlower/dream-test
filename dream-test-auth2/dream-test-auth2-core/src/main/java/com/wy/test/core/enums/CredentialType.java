package com.wy.test.core.enums;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 密码凭证类型
 * 
 * @author 飞花梦影
 * @date 2024-07-30 21:34:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CredentialType {

	USER_DEFINED,
	SHARED,
	SYSTEM,
	NONE;

	public static CredentialType get(int code) {
		return Stream.of(values()).filter(t -> t.ordinal() == code).findFirst().orElse(null);
	}

	public static CredentialType get(String code) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(code)).findFirst().orElse(null);
	}
}