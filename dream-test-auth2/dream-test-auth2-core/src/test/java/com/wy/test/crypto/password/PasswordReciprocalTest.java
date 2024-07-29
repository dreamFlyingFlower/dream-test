package com.wy.test.crypto.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.wy.test.core.crypto.password.PasswordReciprocal;

public class PasswordReciprocalTest {

	public PasswordReciprocalTest() {

	}

	public static void main(String[] args) {
		BCryptPasswordEncoder spe = new BCryptPasswordEncoder();
		// String pass=PasswordReciprocal.getInstance().rawPassword("admin", "admin");
		String pass = "x8zPbCya";
		String epass = spe.encode(pass);
		System.out.println("PasswordEncoder " + epass);

		String encode = PasswordReciprocal.getInstance().encode(pass);
		System.out.println(encode);
		System.out.println(PasswordReciprocal.getInstance().decoder(encode));

		System.out.println(PasswordReciprocal.getInstance().matches(pass, encode));
	}

}
