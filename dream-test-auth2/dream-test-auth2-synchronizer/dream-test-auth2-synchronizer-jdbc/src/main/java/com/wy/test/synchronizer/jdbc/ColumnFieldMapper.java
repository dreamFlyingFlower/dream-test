package com.wy.test.synchronizer.jdbc;

import dream.flying.flower.db.TableColumn;

public class ColumnFieldMapper extends TableColumn {

	String field;

	public ColumnFieldMapper(String column, String field, String type) {
		super(column, type, 0, 0);
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}