package com.wy.test.protocol.cas.endpoint.ticket;

/**
 * Represents a service which wishes to use the CAS protocol.
 */
public final class SimpleWebApplicationServiceImpl extends AbstractWebApplicationService {

	public SimpleWebApplicationServiceImpl(final String id) {
		this(id, id, null);
	}

	private SimpleWebApplicationServiceImpl(final String id, final String originalUrl, final String artifactId) {
		super(id, originalUrl, artifactId);
	}

}
