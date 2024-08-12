package com.wy.test.core.enums;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Saml绑定类型
 * 
 * @author 飞花梦影
 * @date 2024-07-30 21:34:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SamlBindingType {

	REDIRECT_POST("Redirect-Post"),

	REDIRECT_POST_SIMPLE_SIGN("Redirect-PostSimpleSign"),

	POST_POST("Post-Post"),

	POST_POST_SIMPLE_SIGN("Post-PostSimpleSign"),

	IDP_INIT_POST("IdpInit-Post"),

	IDP_INIT_POST_SIMPLE_SIGN("IdpInit-PostSimpleSign");

	private String msg;

	public static SamlBindingType get(int code) {
		return Stream.of(values()).filter(t -> t.ordinal() == code).findFirst().orElse(null);
	}

	public static SamlBindingType get(String code) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(code)).findFirst().orElse(null);
	}
}