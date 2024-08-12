package com.wy.test.authentication.provider.authn.support.rememberme;

import java.io.Serializable;
import java.util.Date;

public class RemeberMe implements Serializable {

	private static final long serialVersionUID = 8010496585233991785L;

	String id;

	String userId;

	String username;

	Date lastLoginTime;

	Date expirationTime;

	public RemeberMe() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RemeberMe [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", username=");
		builder.append(username);
		builder.append(", lastLoginTime=");
		builder.append(lastLoginTime);
		builder.append(", expirationTime=");
		builder.append(expirationTime);
		builder.append("]");
		return builder.toString();
	}
}
