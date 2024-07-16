package com.wy.test.synchronizer.feishu.entity;

import com.wy.test.synchronizer.core.synchronizer.entity.ResponseData;

public class FeishuUsersResponse extends ResponseData {

	FeishuUsersData data;

	public FeishuUsersResponse() {
		super();
	}

	public FeishuUsersData getData() {
		return data;
	}

	public void setData(FeishuUsersData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeishuUsersResponse [data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
