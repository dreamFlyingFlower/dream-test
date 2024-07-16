package com.wy.test.cas.authz.endpoint.ticket;

/**
 * Generic ticket exception. Top of the AbstractTicketException hierarchy.
 */
public abstract class AbstractTicketException {

	String code;

	String msg;

	Throwable throwable;

	/**
	 * Instantiates a new ticket exception.
	 *
	 * @param code the code
	 * @param throwable the throwable
	 */
	public AbstractTicketException(final String code, final Throwable throwable) {
		this.code = code;
		this.throwable = throwable;
	}

	/**
	 * Instantiates a new ticket exception.
	 *
	 * @param code the code
	 */
	public AbstractTicketException(final String code) {
		this.code = code;
	}

	/**
	 * Instantiates a new ticket exception.
	 *
	 * @param code the code
	 * @param msg the msg
	 */
	public AbstractTicketException(final String code, final String msg) {
		this.code = code;
		this.msg = msg;
	}
}
