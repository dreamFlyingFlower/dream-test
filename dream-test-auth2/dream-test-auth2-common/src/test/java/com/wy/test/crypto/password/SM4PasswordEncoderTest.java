package com.wy.test.crypto.password;

public class SM4PasswordEncoderTest {

	public static void main(String[] args) {
		Md4PasswordEncoder sm4 = new Md4PasswordEncoder();
		System.out.println(sm4.encode("maxkeypassword"));

		String c = "{BQWoTG+C4jL8d8QNIu0jL1WkMWezxNAZtliNoJOke5k=}8cfc46546a5996e74442183bd122f370";
		System.out.println(sm4.matches("maxkeypassword", c));
	}

}
