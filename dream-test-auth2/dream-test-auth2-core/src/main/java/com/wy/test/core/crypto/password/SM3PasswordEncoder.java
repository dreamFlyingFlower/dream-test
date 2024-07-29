package com.wy.test.core.crypto.password;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

import dream.flying.flower.framework.core.crypto.SM3Helpers;

/**
 * SM3加密
 *
 * @author 飞花梦影
 * @date 2024-07-20 09:33:33
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SM3PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return new String(Hex.encode(SM3Helpers.encode(rawPassword.toString().getBytes())));
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(encode(rawPassword));
	}
}