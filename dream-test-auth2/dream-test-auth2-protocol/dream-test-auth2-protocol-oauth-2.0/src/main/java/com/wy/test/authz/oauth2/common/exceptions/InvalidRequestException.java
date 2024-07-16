package com.wy.test.authz.oauth2.common.exceptions;

@SuppressWarnings("serial")
public class InvalidRequestException extends ClientAuthenticationException {

	public InvalidRequestException(String msg, Throwable t) {
		super(msg, t);
	}

	public InvalidRequestException(String msg) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_request";
	}
}
