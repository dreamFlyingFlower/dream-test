package com.wy.test.web.apis.identity.scim.resources;

import java.util.HashSet;
import java.util.Set;

public class ScimGroup extends ScimResource {

	private static final long serialVersionUID = 404613567384513866L;

	public static final String SCHEMA = "urn:ietf:params:scim:schemas:core:2.0:Group";

	private String displayName;

	private Set<ScimMemberRef> members;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Set<ScimMemberRef> getMembers() {
		return members;
	}

	public void setMembers(Set<ScimMemberRef> members) {
		this.members = members;
	}

	public ScimGroup() {
		schemas = new HashSet<String>();
		schemas.add(SCHEMA);
	}

}
