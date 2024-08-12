package com.wy.test.sync.weixin.entity;

import java.util.ArrayList;

import com.wy.test.sync.core.synchronizer.entity.ResponseData;

public class WorkWeixinDeptsResponse extends ResponseData {

	ArrayList<WorkWeixinDepts> department;

	public ArrayList<WorkWeixinDepts> getDepartment() {
		return department;
	}

	public void setDepartment(ArrayList<WorkWeixinDepts> department) {
		this.department = department;
	}

	public WorkWeixinDeptsResponse() {
		super();
	}

}
