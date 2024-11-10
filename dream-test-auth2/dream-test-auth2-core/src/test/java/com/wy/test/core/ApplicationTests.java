package com.wy.test.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import dream.flying.flower.binary.Base64Helper;

//@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args) {
		System.out.println(Base64Helper.encodeString("messaging-client:123456".getBytes()));
	}
}
