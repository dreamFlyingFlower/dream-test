package com.wy.test.sync.feishu.entity;

import java.util.ArrayList;

public class FeishuUsersData {

	boolean has_more;

	String page_token;

	ArrayList<FeishuUsers> items;

	public FeishuUsersData() {
		super();
	}

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

	public ArrayList<FeishuUsers> getItems() {
		return items;
	}

	public void setItems(ArrayList<FeishuUsers> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeishuUsersData [has_more=");
		builder.append(has_more);
		builder.append(", page_token=");
		builder.append(page_token);
		builder.append(", items=");
		builder.append(items);
		builder.append("]");
		return builder.toString();
	}

}
