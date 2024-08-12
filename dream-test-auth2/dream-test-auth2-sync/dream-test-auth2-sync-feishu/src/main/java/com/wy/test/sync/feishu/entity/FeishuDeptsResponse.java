package com.wy.test.sync.feishu.entity;

import com.wy.test.sync.core.synchronizer.entity.ResponseData;

public class FeishuDeptsResponse extends ResponseData {

	FeishuDeptsData data;

	public FeishuDeptsResponse() {
		super();
	}

	public FeishuDeptsData getData() {
		return data;
	}

	public void setData(FeishuDeptsData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeishuDeptsResponse [data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
