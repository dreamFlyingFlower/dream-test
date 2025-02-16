package com.wy.test.core.enums;

import java.util.stream.Stream;

import dream.flying.flower.common.CodeMsg;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台角色类型
 *
 * @author 飞花梦影
 * @date 2024-07-31 15:01:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PlatformRoleType implements CodeMsg {

	/**
	 * 平台超级管理员
	 */
	PLATFORM_ADMIN("PLATFORM_ADMIN"),

	/**
	 * 租户管理员
	 */
	TANANT_ADMIN("TANANT_ADMIN"),

	/**
	 * 普通用户
	 */
	ORDINARY_USER("ORDINARY_USER");

	private final String msg;

	@Override
	public Integer getValue() {
		return this.ordinal();
	}

	public static PlatformRoleType get(String msg) {
		return Stream.of(values()).filter(t -> t.getMsg().equals(msg)).findFirst().orElse(null);
	}
}