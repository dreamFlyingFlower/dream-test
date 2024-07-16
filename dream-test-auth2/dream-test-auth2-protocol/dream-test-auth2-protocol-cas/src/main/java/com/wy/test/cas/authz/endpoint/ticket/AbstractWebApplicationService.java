package com.wy.test.cas.authz.endpoint.ticket;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a WebApplicationService.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
public abstract class AbstractWebApplicationService {

	/** Logger instance. **/
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebApplicationService.class);

	private static final Map<String, Object> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<String, Object>());

	/** The id of the service. */
	private final String id;

	/** The original url provided, used to reconstruct the redirect url. */
	private final String originalUrl;

	private final String artifactId;

	private boolean loggedOutAlready = false;

	protected AbstractWebApplicationService(final String id, final String originalUrl, final String artifactId) {
		this.id = id;
		this.originalUrl = originalUrl;
		this.artifactId = artifactId;
	}

	@Override
	public final String toString() {
		return this.id;
	}

	public final String getId() {
		return this.id;
	}

	public final String getArtifactId() {
		return this.artifactId;
	}

	public final Map<String, Object> getAttributes() {
		return EMPTY_MAP;
	}

	protected static String cleanupUrl(final String url) {
		if (url == null) {
			return null;
		}

		final int jsessionPosition = url.indexOf(";jsession");

		if (jsessionPosition == -1) {
			return url;
		}

		final int questionMarkPosition = url.indexOf("?");

		if (questionMarkPosition < jsessionPosition) {
			return url.substring(0, url.indexOf(";jsession"));
		}

		return url.substring(0, jsessionPosition) + url.substring(questionMarkPosition);
	}

	/**
	 * Return the original url provided (as <code>service</code> or
	 * <code>targetService</code> request parameter). Used to reconstruct the
	 * redirect url.
	 *
	 * @return the original url provided.
	 */
	public final String getOriginalUrl() {
		return this.originalUrl;
	}

	@Override
	public boolean equals(final Object object) {
		if (object == null) {
			return false;
		}

		if (object instanceof Service) {
			final Service service = (Service) object;

			return getId().equals(service.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 41;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	public boolean matches(final Service service) {
		return this.id.equals(service.getId());
	}

	/**
	 * Return if the service is already logged out.
	 *
	 * @return if the service is already logged out.
	 */
	public boolean isLoggedOutAlready() {
		return loggedOutAlready;
	}

	/**
	 * Set if the service is already logged out.
	 *
	 * @param loggedOutAlready if the service is already logged out.
	 */
	public final void setLoggedOutAlready(final boolean loggedOutAlready) {
		this.loggedOutAlready = loggedOutAlready;
	}
}
