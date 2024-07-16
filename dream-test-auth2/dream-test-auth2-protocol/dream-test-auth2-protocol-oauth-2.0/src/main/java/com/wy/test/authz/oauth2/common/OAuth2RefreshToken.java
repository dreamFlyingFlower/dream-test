package com.wy.test.authz.oauth2.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

public interface OAuth2RefreshToken  extends Serializable {

	/**
	 * The value of the token.
	 * 
	 * @return The value of the token.
	 */
	@JsonValue
	String getValue();

}