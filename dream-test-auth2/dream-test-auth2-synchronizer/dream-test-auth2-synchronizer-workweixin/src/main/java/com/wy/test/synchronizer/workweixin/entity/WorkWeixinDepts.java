package com.wy.test.synchronizer.workweixin.entity;

public class WorkWeixinDepts {

	long id;

	String name;

	String name_en;

	long parentid;

	long order;

	public WorkWeixinDepts() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public long getParentid() {
		return parentid;
	}

	public void setParentid(long parentid) {
		this.parentid = parentid;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}

}
