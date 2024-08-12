package com.wy.test.core.enums;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 婚姻类型
 * 
 * @author 飞花梦影
 * @date 2024-07-30 21:34:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MarryType {

	/** 未知 */
	UNKNOWN,

	/** 单身 */
	SINGLE,

	/** 结婚 */
	MARRIED,

	/** 离异 */
	DIVORCE,

	/** 丧偶 */
	WIDOWED;

	public static MarryType get(int code) {
		return Stream.of(values()).filter(t -> t.ordinal() == code).findFirst().orElse(null);
	}

	public static MarryType get(String code) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(code)).findFirst().orElse(null);
	}
}