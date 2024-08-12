package com.wy.test.sync.weixin.entity;

import java.util.ArrayList;

import com.wy.test.sync.core.synchronizer.entity.ResponseData;

public class WeixinUsersResponse extends ResponseData {

	ArrayList<WeixinUsers> userlist;

	public WeixinUsersResponse() {
		super();
	}

	public ArrayList<WeixinUsers> getUserlist() {
		return userlist;
	}

	public void setUserlist(ArrayList<WeixinUsers> userlist) {
		this.userlist = userlist;
	}

}
