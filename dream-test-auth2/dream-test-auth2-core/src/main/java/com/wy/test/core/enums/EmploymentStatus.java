package com.wy.test.core.enums;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 就业状态
 * 
 * @author 飞花梦影
 * @date 2024-07-30 21:34:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum EmploymentStatus {

	/** 离职人员 */
	WITHDRAWN,

	/** 在册人员 */
	ACTIVE,

	/** 停薪留职人员 */
	INACTIVE,

	/** 退休人员 */
	RETIREE;

	public static EmploymentStatus get(int code) {
		return Stream.of(values()).filter(t -> t.ordinal() == code).findFirst().orElse(null);
	}

	public static EmploymentStatus get(String code) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(code)).findFirst().orElse(null);
	}
}