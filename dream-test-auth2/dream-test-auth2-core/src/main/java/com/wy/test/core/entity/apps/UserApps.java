package com.wy.test.core.entity.apps;

/**
 * UserApps .
 */
public class UserApps extends Apps {

	private static final long serialVersionUID = 3186085827268041549L;

	private String username;

	private String userId;

	private String displayName;

	public UserApps() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserApps [username=");
		builder.append(username);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append("]");
		return builder.toString();
	}

}
