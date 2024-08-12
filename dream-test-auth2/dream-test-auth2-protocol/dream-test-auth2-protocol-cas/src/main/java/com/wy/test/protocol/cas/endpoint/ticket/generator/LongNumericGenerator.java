package com.wy.test.protocol.cas.endpoint.ticket.generator;

/**
 * Interface to guaranteed to return a long.
 */
public interface LongNumericGenerator extends NumericGenerator {

	/**
	 * Get the next long in the sequence.
	 *
	 * @return the next long in the sequence.
	 */
	long getNextLong();
}
