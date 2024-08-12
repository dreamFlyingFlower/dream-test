package com.wy.test.sync.feishu.entity;

public class FeishuDeptStatus {

	boolean is_deleted;

	public FeishuDeptStatus() {
		super();

	}

	public boolean isIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeishuDeptStatus [is_deleted=");
		builder.append(is_deleted);
		builder.append("]");
		return builder.toString();
	}

}
