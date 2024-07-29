package com.wy.test.core.uuid;

import java.security.*;

public final class UUIDRandomness {

	static SecureRandom random = new SecureRandom();

	private UUIDRandomness() {
		throw new Error();
	}

	public static byte[] randomNodeID() {
		byte[] id = new byte[6];
		synchronized (random) {
			random.nextBytes(id);
		}
		id[0] |= 0x01;
		return id;
	}

	public static int randomClockSequence() {
		synchronized (random) {
			return random.nextInt(16384);
		}
	}

	public static int nextRandomClockSequence(int prev) {
		int next;
		synchronized (random) {
			next = random.nextInt(16383);
		}
		if (next >= prev)
			next++;
		return next;
	}
}
