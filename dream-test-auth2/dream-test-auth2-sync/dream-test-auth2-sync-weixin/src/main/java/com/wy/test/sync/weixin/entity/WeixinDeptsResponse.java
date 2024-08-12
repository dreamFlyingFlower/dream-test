package com.wy.test.sync.weixin.entity;

import java.util.ArrayList;

import com.wy.test.sync.core.synchronizer.entity.ResponseData;

public class WeixinDeptsResponse extends ResponseData {

	ArrayList<WeixinDepts> department;

	public ArrayList<WeixinDepts> getDepartment() {
		return department;
	}

	public void setDepartment(ArrayList<WeixinDepts> department) {
		this.department = department;
	}

	public WeixinDeptsResponse() {
		super();
	}

}
