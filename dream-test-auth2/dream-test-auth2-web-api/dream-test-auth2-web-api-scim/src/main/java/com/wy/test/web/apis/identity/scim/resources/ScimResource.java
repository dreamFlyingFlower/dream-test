package com.wy.test.web.apis.identity.scim.resources;

import java.io.Serializable;
import java.util.Set;

public class ScimResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5159743553948621024L;

	protected Set<String> schemas;

	private String id;

	private String externalId;

	private ScimMeta meta;

	public ScimResource() {

	}

	public ScimResource(String id, String externalId, ScimMeta meta, Set<String> schemas) {
		super();
		this.id = id;
		this.externalId = externalId;
		this.meta = meta;
		this.schemas = schemas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public ScimMeta getMeta() {
		return meta;
	}

	public void setMeta(ScimMeta meta) {
		this.meta = meta;
	}

	public Set<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(Set<String> schemas) {
		this.schemas = schemas;
	}

}
