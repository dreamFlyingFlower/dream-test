package com.wy.test.entity;

public class DbTableColumn {

	String column;

	String type;

	int precision;

	int scale;

	public DbTableColumn(String column, String type, int precision, int scale) {
		super();
		this.column = column;
		this.type = type;
		this.precision = precision;
		this.scale = scale;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

}
