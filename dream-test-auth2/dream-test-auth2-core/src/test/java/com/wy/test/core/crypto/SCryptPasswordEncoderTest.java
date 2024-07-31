package com.wy.test.core.crypto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SCryptPasswordEncoderTest {

	public SCryptPasswordEncoderTest() {
	}

	public static void main(String[] args) {
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		// String
		// c="$e0801$7Holo9EgzBeg5xf/WLZu3/5IQwOyEPDLJPgMXkF9jnekBrbQUMt4CF9O2trkz3zBCnCLpUMR437q/AjQ5TTToA==$oYB8KRSxAsxkKkt5r79W6r6P0wTUcKwGye1ivXRN0Ts="
		// ;
		System.out.println(pe.encode("admin"));
		// System.out.println(pe.encode("shimingxy")+"_password");
		// System.out.println(pe.matches("shimingxy"+"_password", c));
	}

}
