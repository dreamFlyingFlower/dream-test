package com.wy.test.core.util;

import java.util.Date;
// import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.wy.test.core.uuid.UUID;

import dream.flying.flower.generator.UuidGenerator;

public class UUIDGeneratorTest {

	@Test
	public void test() {
		Date sd = new Date();

		// for(int i=0;i<100000;i++){
		UuidGenerator generated = new UuidGenerator();
		generated.toString();
		// System.out.println(generated.toString());

		// }
		Date ed = new Date();
		System.out.println("usertime " + (ed.getTime() - sd.getTime()));

		// UUIDGenerator.version(generated);

		System.out.println("JDK UUID");
		Date ssd = new Date();
		// for(int i=0;i<100000;i++){
		// UUID.randomUUID().toString();
		UUID.generate().toString();
		// System.out.println(UUID.randomUUID().toString());
		// }
		Date sed = new Date();
		System.out.println("usertime " + (sed.getTime() - ssd.getTime()));

		UuidGenerator.version(new UuidGenerator(UUID.generate().toString()));
	}
}