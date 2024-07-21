package com.wy.test.common.uuid;

/**
 * A more solid timestamp-based UUID generator, this one keeps its timestamps
 * within a millisecond or so of the current time, and is thread-safe.
 */
public class TimestampUUIDGenerator extends UnsynchronizedTimestampUUIDGenerator implements UUIDGenerator {

	/**
	 * Creates a UUIDGenerator with the specified clock sequence number and node ID.
	 *
	 * @throws NullPointerException if node == null
	 * @throws IllegalArgumentException if clock_sequence is out of range or
	 *         node.length != 6
	 */
	public TimestampUUIDGenerator(int clock_sequence, byte[] node) {
		super(clock_sequence, node);
	}

	/**
	 * Generates a new UUID.
	 *
	 * @throws IllegalStateException if adjustmentOverflow() throws it
	 */
	@Override
	public UUID nextUUID() {
		synchronized (this) {
			checkSystemTime();
			return super.nextUUID();
		}
	}

}
