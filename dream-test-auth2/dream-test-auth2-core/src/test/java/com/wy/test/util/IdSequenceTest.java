package com.wy.test.util;

public class IdSequenceTest {

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		int k;
		for (int i = 1; i <= 10010; i++) {
			k = (i) % 10000;
			System.out.println(k);
		}
		System.out.println(System.currentTimeMillis() - s);
	}

}
