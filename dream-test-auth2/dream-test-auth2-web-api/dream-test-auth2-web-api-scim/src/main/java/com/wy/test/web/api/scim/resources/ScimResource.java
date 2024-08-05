package com.wy.test.web.api.scim.resources;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ScimResource implements Serializable {

	private static final long serialVersionUID = -5159743553948621024L;

	private String id;

	private String externalId;

	private ScimMeta meta;

	protected Set<String> schemas;
}