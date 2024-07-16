package com.wy.test.authz.oauth2.provider;

@SuppressWarnings("serial")
public class NoSuchClientException extends ClientRegistrationException {

	public NoSuchClientException(String msg) {
		super(msg);
	}

	public NoSuchClientException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
