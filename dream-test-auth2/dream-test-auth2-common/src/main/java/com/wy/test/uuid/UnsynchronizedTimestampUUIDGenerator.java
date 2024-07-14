package com.wy.test.uuid;

/**
 * As simple and fast of a timestamp-based UUIDGenerator as is practical. This
 * class is not thread-safe, and the timestamp field in its UUIDs is not
 * reliably close to the actual time.
 */
public class UnsynchronizedTimestampUUIDGenerator implements UUIDGenerator {

	/**
	 * Number of milliseconds between the Gregorian calendar cutover and the Unix
	 * epoch.
	 */
	public static final long EPOCH_OFFSET = 12219292800000L;

	/**
	 * Number of units of UUID time resolution per unit of system clock resolution.
	 */
	public static final long CLOCK_RES = 10000L;

	protected long last_time;

	protected long clock_adj;

	protected int clock_sequence;

	protected byte[] node;

	/**
	 * Creates a UUIDGenerator with the specified clock sequence number and node ID.
	 *
	 * @throws NullPointerException if node == null
	 * @throws IllegalArgumentException if clock_sequence is out of range or
	 *         node.length != 6
	 */
	public UnsynchronizedTimestampUUIDGenerator(int clock_sequence, byte[] node) {
		if (clock_sequence < 0 || clock_sequence >= 16384)
			throw new IllegalArgumentException();
		if (node == null)
			throw new NullPointerException();
		if (node.length != 6)
			throw new IllegalArgumentException();

		this.clock_sequence = clock_sequence;
		this.node = (byte[]) node.clone();
		checkSystemTime();
	}

	/**
	 * Reads the current system time and updates last_time, clock_sequence and
	 * clock_adj based on it.
	 */
	protected void checkSystemTime() {
		long sys_time = System.currentTimeMillis();

		/* If monotonicity is lost, bump clock_sequence. */
		if (sys_time < last_time)
			clock_sequence = UUIDRandomness.nextRandomClockSequence(clock_sequence);

		/* If the clock ticked, clear the adjustment. */
		if (sys_time != last_time) {
			last_time = sys_time;
			clock_adj = 0;
		}
	}

	/**
	 * Called when clock_adj >= CLOCK_RES, expected to take corrective action. May
	 * throw an IllegalStateException if corrective action fails.
	 */
	protected void adjustmentOverflow() throws IllegalStateException {
		checkSystemTime();
		if (clock_adj >= CLOCK_RES)
			throw new IllegalStateException();
	}

	/**
	 * Generates a new UUID.
	 *
	 * @throws IllegalStateException if adjustmentOverflow() throws it
	 */
	public UUID nextUUID() {
		long unique_time = (last_time + EPOCH_OFFSET) * CLOCK_RES + clock_adj;
		if (++clock_adj > CLOCK_RES)
			adjustmentOverflow();

		return new UUID((int) (unique_time & 0xFFFFFFFF), (short) ((unique_time >> 32) & 0xFFFF),
				(short) (((unique_time >> 48) & 0x0FFF) | UUID.VERSION_TIMESTAMP), (byte) (clock_sequence & 0xFF),
				(byte) (((clock_sequence >> 8) & 0x3F) | UUID.VARIANT_DCE), node);
	}
}
