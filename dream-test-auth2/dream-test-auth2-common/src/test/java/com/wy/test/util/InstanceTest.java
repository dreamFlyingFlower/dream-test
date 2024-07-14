package com.wy.test.util;

import java.security.Provider;
import java.security.Security;

import com.wy.test.crypto.password.PasswordReciprocal;

public class InstanceTest {

	public static void main(String[] args) {
		if (System.getProperty("java.version").startsWith("1.8")) {
			System.out.println("1.8");
			Security.addProvider((Provider) Instance.newInstance("com.sun.crypto.provider.SunJCE"));
			System.out.println(PasswordReciprocal.getInstance().encode("ddddd"));

			System.out.println(PasswordReciprocal.getInstance().encode("ddfs"));
		} else {
			System.out.println("other");
		}

	}

}
