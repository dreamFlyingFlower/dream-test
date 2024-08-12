package com.wy.test.sync.core.synchronizer.entity;

public class AccessToken {

	int errcode;

	String errmsg;

	String access_token;

	// feishu access_token
	String tenant_access_token;

	String expires_in;

	public AccessToken() {
		super();
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getTenant_access_token() {
		return tenant_access_token;
	}

	public void setTenant_access_token(String tenant_access_token) {
		this.tenant_access_token = tenant_access_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccessToken [errcode=");
		builder.append(errcode);
		builder.append(", errmsg=");
		builder.append(errmsg);
		builder.append(", access_token=");
		builder.append(access_token);
		builder.append(", expires_in=");
		builder.append(expires_in);
		builder.append("]");
		return builder.toString();
	}

}
