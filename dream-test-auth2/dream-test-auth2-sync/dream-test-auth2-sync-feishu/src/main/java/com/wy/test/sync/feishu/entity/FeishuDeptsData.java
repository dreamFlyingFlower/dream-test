package com.wy.test.sync.feishu.entity;

import java.util.ArrayList;

import com.wy.test.sync.core.synchronizer.entity.ResponseData;

public class FeishuDeptsData extends ResponseData {

	boolean has_more;

	String page_token;

	ArrayList<FeishuDepts> items;

	FeishuDepts department;

	public boolean isHas_more() {
		return has_more;
	}

	public void setHas_more(boolean has_more) {
		this.has_more = has_more;
	}

	public String getPage_token() {
		return page_token;
	}

	public void setPage_token(String page_token) {
		this.page_token = page_token;
	}

	public ArrayList<FeishuDepts> getItems() {
		return items;
	}

	public void setItems(ArrayList<FeishuDepts> items) {
		this.items = items;
	}

	public FeishuDepts getDepartment() {
		return department;
	}

	public void setDepartment(FeishuDepts department) {
		this.department = department;
	}

	public FeishuDeptsData() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeishuDeptsResponse [has_more=");
		builder.append(has_more);
		builder.append(", page_token=");
		builder.append(page_token);
		builder.append(", items=");
		builder.append(items);
		builder.append("]");
		return builder.toString();
	}

}
