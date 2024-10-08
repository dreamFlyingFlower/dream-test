package com.wy.test.core.util;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import dream.flying.flower.generator.SnowFlakeGenerator;

public class SonwFlakeIdTest {

	@Test
	public void UidGenerator() {
		DateTime d = new DateTime("2020-01-01T01:01:01");
		System.out.println("time " + d.getMillis());
		SnowFlakeGenerator snowFlake = new SnowFlakeGenerator(1, 1, 8, d.getMillis());
		long seq = snowFlake.nextId();

		System.out.println(seq);
		System.out.println(snowFlake.parse(seq).getDateTime());
	}

	@Test
	public void performance() {
		SnowFlakeGenerator snowFlake = new SnowFlakeGenerator(1, 1);

		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			System.out.println(snowFlake.nextId());
		}

		System.out.println(System.currentTimeMillis() - start);
	}
}
