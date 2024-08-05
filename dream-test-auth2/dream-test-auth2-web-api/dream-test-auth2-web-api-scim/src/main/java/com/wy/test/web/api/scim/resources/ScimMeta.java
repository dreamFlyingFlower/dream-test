package com.wy.test.web.api.scim.resources;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import dream.flying.flower.ConstDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

	public ScimMeta(String resourceType) {
		this.resourceType = resourceType;
		this.version = "1.0";
	}
}