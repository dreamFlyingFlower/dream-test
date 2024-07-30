package com.wy.test.core.password;

import java.security.MessageDigest;

import org.springframework.security.crypto.codec.Utf8;

/**
 * Utility for constant time comparison to prevent against timing attacks.
 *
 * @author 飞花梦影
 * @date 2024-07-14 21:37:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class PasswordEncoderUtils {

	/**
	 * Constant time comparison to prevent against timing attacks.
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	static boolean equals(String expected, String actual) {
		byte[] expectedBytes = bytesUtf8(expected);
		byte[] actualBytes = bytesUtf8(actual);

		return MessageDigest.isEqual(expectedBytes, actualBytes);
	}

	private static byte[] bytesUtf8(String s) {
		if (s == null) {
			return null;
		}

		return Utf8.encode(s);
	}

	private PasswordEncoderUtils() {
	}
}
