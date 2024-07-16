package com.wy.test.web.apis.identity.scim.resources;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import dream.flying.flower.ConstDate;

public class ScimMeta implements Serializable {

	private static final long serialVersionUID = -2244662962968933591L;

	private String resourceType;

	@JsonFormat(pattern = ConstDate.DATETIME_ISO)
	@DateTimeFormat(pattern = ConstDate.DATETIME_ISO)
	private Date created;

	@JsonFormat(pattern = ConstDate.DATETIME_ISO)
	@DateTimeFormat(pattern = ConstDate.DATETIME_ISO)
	private Date lastModified;

	private String location;

	private String version;

	private Set<String> attributes;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Set<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<String> attributes) {
		this.attributes = attributes;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public ScimMeta() {

	}

	public ScimMeta(String resourceType) {
		this.resourceType = resourceType;
		this.version = "1.0";
	}

	public ScimMeta(String resourceType, Date created, Date lastModified, String location, String version,
			Set<String> attributes) {
		super();
		this.resourceType = resourceType;
		this.created = created;
		this.lastModified = lastModified;
		this.location = location;
		this.version = version;
		this.attributes = attributes;
	}

}
