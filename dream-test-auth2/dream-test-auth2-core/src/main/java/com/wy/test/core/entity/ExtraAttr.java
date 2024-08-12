package com.wy.test.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtraAttr {

	private String attr;

	private String type;

	private String value;

	public ExtraAttr(String attr, String value) {
		super();
		this.attr = attr;
		this.value = value;
	}
}