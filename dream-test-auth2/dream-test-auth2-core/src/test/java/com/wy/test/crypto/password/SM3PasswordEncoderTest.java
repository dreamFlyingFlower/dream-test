package com.wy.test.crypto.password;

import com.wy.test.core.crypto.password.SM3PasswordEncoder;

public class SM3PasswordEncoderTest {

	public static void main(String[] args) {
		SM3PasswordEncoder sm3 = new SM3PasswordEncoder();
		System.out.println(sm3.encode("dreampassword"));

		String c = "f4679d46e96d95d67db4c8c91fcf8aaaa4e1d437ffee278d2ea97f41f7f48c12";
		System.out.println(sm3.matches("dreampassword", c));
	}

}
