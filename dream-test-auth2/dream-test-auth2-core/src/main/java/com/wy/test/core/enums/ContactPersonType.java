package com.wy.test.core.enums;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 联系人类型
 * 
 * @author 飞花梦影
 * @date 2024-07-30 21:34:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ContactPersonType {

	TECHNICAL,
	SUPPORT,
	ADMINISTRATIVE,
	BILLING,
	OTHER;

	public static ContactPersonType get(int code) {
		return Stream.of(values()).filter(t -> t.ordinal() == code).findFirst().orElse(null);
	}

	public static ContactPersonType get(String code) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(code)).findFirst().orElse(null);
	}
}