package com.wy.test.sync.weixin.entity;

import java.util.ArrayList;

import com.wy.test.sync.core.synchronizer.entity.ResponseData;

public class WorkWeixinUsersResponse extends ResponseData {

	ArrayList<WorkWeixinUsers> userlist;

	public WorkWeixinUsersResponse() {
		super();
	}

	public ArrayList<WorkWeixinUsers> getUserlist() {
		return userlist;
	}

	public void setUserlist(ArrayList<WorkWeixinUsers> userlist) {
		this.userlist = userlist;
	}

}
