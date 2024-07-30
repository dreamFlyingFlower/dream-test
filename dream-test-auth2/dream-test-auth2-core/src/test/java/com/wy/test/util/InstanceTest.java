package com.wy.test.util;

import java.lang.reflect.InvocationTargetException;
import java.security.Provider;
import java.security.Security;

import com.wy.test.core.password.PasswordReciprocal;

import dream.flying.flower.reflect.ReflectHelper;

public class InstanceTest {

	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (System.getProperty("java.version").startsWith("1.8")) {
			System.out.println("1.8");
			Security.addProvider((Provider) ReflectHelper.newInstance("com.sun.crypto.provider.SunJCE"));
			System.out.println(PasswordReciprocal.getInstance().encode("ddddd"));
			System.out.println(PasswordReciprocal.getInstance().encode("ddfs"));
		} else {
			System.out.println("other");
		}
	}
}