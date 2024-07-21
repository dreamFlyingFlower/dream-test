package com.wy.test.crypto;

import com.wy.test.common.crypto.password.PasswordGen;

public class PasswordGenTest {

	public PasswordGenTest() {
	}

	public static void main(String[] args) {
		PasswordGen gen = new PasswordGen();
		System.out.println(gen.gen(2, 2, 2, 1));
		for (int i = 1; i < 100; i++) {
			// System.out.println(gen.gen());
			// System.out.println(gen.gen(6));
			// System.out.println(gen.gen(2,2,2,0));
		}

	}

}
