package com.wy.test.core.password;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;

/**
 * PasswordReciprocal.
 * 
 * @author 飞花梦影
 * @date 2024-07-14 21:37:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class PasswordReciprocal implements PasswordEncoder {

	public static int PREFFIX_LENGTH = 7;

	public static PasswordReciprocal passwordReciprocal;

	public PasswordReciprocal() {

	}

	/**
	 * getInstance.
	 * 
	 * @return
	 */
	public static PasswordReciprocal getInstance() {

		if (passwordReciprocal == null) {
			passwordReciprocal = new PasswordReciprocal();
		}

		return passwordReciprocal;
	}

	public String decoder(CharSequence encodedPassword) {
		String salt = encodedPassword.subSequence(0, 29).toString();
		encodedPassword = encodedPassword.subSequence(29, encodedPassword.length());
		String plain = ReciprocalHelpers.decoderHex(encodedPassword.toString(), salt.substring(PREFFIX_LENGTH));
		return plain.substring(salt.substring(PREFFIX_LENGTH).length());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String salt = encodedPassword.subSequence(0, 29).toString();
		String finalPassword = encode(rawPassword, salt);
		return finalPassword.equals(encodedPassword);// ReciprocalUtils.encode(rawPassword.toString()).equals(encodedPassword);
	}

	/**
	 * salt length 29
	 * 
	 * @return salt
	 */
	public String gensalt() {
		return BCrypt.gensalt("$2a", 10);
	}

	@Override
	public String encode(CharSequence plain) {
		// $2a$10$
		String salt = gensalt();
		return encode(plain, salt);
	}

	private String encode(CharSequence plain, String salt) {
		String password = salt.substring(PREFFIX_LENGTH) + plain;
		return salt + ReciprocalHelpers.encode2Hex(password, salt.substring(PREFFIX_LENGTH));
	}
}