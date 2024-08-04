package com.wy.test.core.entity;

public class ExtraAttr {

	String attr;

	String type;

	String value;

	public ExtraAttr() {
		super();
	}

	public ExtraAttr(String attr, String value) {
		super();
		this.attr = attr;
		this.value = value;
	}

	/**
	 * @param attr
	 * @param value
	 */
	public ExtraAttr(String attr, String type, String value) {
		super();
		this.attr = attr;
		this.type = type;
		this.value = value;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExtraAttr [attr=");
		builder.append(attr);
		builder.append(", type=");
		builder.append(type);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

}
