package com.wy.test.core.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import dream.flying.flower.common.CodeMsg;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 普通角色类型
 *
 * @author 飞花梦影
 * @date 2024-07-31 15:01:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrdinaryRoleType implements CodeMsg<String> {

	ROLE_ADMINISTRATORS("ROLE_ADMINISTRATORS", new SimpleGrantedAuthority("ROLE_ADMINISTRATORS")),

	ROLE_MANAGERS("ROLE_MANAGERS", new SimpleGrantedAuthority("ROLE_MANAGERS")),

	ROLE_USER("ROLE_USER", new SimpleGrantedAuthority("ROLE_USER")),

	ROLE_ALL_USER("ROLE_ALL_USER", new SimpleGrantedAuthority("ROLE_ALL_USER")),

	ROLE_ORDINARY_USER("ROLE_ORDINARY_USER", new SimpleGrantedAuthority("ROLE_ORDINARY_USER"));

	private final String code;

	private final SimpleGrantedAuthority simpleGrantedAuthority;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return code;
	}
}