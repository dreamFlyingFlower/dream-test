package com.wy.test.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AuthJwkConfig {

	@Value("${dream.auth.jwt.expires:86400}")
	int expires;

	@Value("${dream.auth.jwt.secret}")
	String secret;

	@Value("${dream.session.timeout}")
	int refreshExpires;

	@Value("${dream.auth.jwt.refresh.secret}")
	String refreshSecret;

	@Value("${dream.auth.jwt.issuer:https://sso.dream.top/}")
	String issuer;

	public AuthJwkConfig() {
		super();
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public int getExpires() {
		return expires;
	}

	public void setExpires(int expires) {
		this.expires = expires;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getRefreshExpires() {
		return refreshExpires;
	}

	public void setRefreshExpires(int refreshExpires) {
		this.refreshExpires = refreshExpires;
	}

	public String getRefreshSecret() {
		return refreshSecret;
	}

	public void setRefreshSecret(String refreshSecret) {
		this.refreshSecret = refreshSecret;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthJwkConfig [issuer=");
		builder.append(issuer);
		builder.append(", expires=");
		builder.append(expires);
		builder.append(", secret=");
		builder.append(secret);
		builder.append("]");
		return builder.toString();
	}

}
