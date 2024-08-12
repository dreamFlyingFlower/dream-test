package com.wy.test.protocol.cas.endpoint.ticket.generator;

/**
 * Interface to return a random String.
 */
public interface RandomStringGenerator {

	/**
	 * @return the minimum length as an int guaranteed by this generator.
	 */
	int getMinLength();

	/**
	 * @return the maximum length as an int guaranteed by this generator.
	 */
	int getMaxLength();

	/**
	 * @return the new random string
	 */
	String getNewString();

	/**
	 * Gets the new string as bytes.
	 *
	 * @return the new random string as bytes
	 */
	byte[] getNewStringAsBytes();
}
