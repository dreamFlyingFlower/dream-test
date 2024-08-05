package com.wy.test.web.api.scim.resources;

public class ScimManager {

	private String managerId;

	private String displayName;

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ScimManager() {
	}

	public ScimManager(String managerId, String displayName) {
		super();
		this.managerId = managerId;
		this.displayName = displayName;
	}

}
