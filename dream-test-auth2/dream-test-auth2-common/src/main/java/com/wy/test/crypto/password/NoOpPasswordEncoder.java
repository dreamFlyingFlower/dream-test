package com.wy.test.crypto.password;

import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This {@link PasswordEncoder} is provided for legacy and testing purposes only
 * and is not considered secure.
 *
 * A password encoder that does nothing. Useful for testing where working with
 * plain text passwords may be preferred.
 *
 * @author Keith Donald deprecated This PasswordEncoder is not secure. Instead
 *         use an adaptive one way function like BCryptPasswordEncoder,
 *         Pbkdf2PasswordEncoder, or SCryptPasswordEncoder. Even better use
 *         {@link DelegatingPasswordEncoder} which supports password upgrades.
 *         There are no plans to remove this support. It is deprecated to
 *         indicate that this is a legacy implementation and using it is
 *         considered insecure.
 */

public final class NoOpPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword.toString().equals(encodedPassword);
	}

	/**
	 * Get the singleton {@link NoOpPasswordEncoder}.
	 */
	public static PasswordEncoder getInstance() {
		return INSTANCE;
	}

	private static final PasswordEncoder INSTANCE = new NoOpPasswordEncoder();

	private NoOpPasswordEncoder() {
	}

}
