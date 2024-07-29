package com.wy.test.crypto.password;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

@SuppressWarnings("deprecation")
public class StandardPasswordEncoderTest {

	public static void main(String[] args) {
		StandardPasswordEncoder spe = new StandardPasswordEncoder();
		System.out.println(spe.encode("maxkeypassword"));

		String c = "4b60c81ad4c31d97fbe8c87952f8de7a329ceb004261c8bd22254cfa8aa096bede6efbafcc84bade";
		System.out.println(spe.matches("maxkeypassword", c));
	}
}