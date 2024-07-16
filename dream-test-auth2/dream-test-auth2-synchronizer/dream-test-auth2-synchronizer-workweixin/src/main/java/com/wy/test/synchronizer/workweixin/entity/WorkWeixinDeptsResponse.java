package com.wy.test.synchronizer.workweixin.entity;

import java.util.ArrayList;

import com.wy.test.synchronizer.core.synchronizer.entity.ResponseData;

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
