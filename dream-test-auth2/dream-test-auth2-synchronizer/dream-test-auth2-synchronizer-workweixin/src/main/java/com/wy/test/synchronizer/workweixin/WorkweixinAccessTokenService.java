package com.wy.test.synchronizer.workweixin;

import com.wy.test.common.util.JsonUtils;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.synchronizer.core.synchronizer.entity.AccessToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkweixinAccessTokenService {

	String corpid;

	String corpsecret;

	public static String TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

	public WorkweixinAccessTokenService() {
	}

	public WorkweixinAccessTokenService(String corpid, String corpsecret) {
		super();
		this.corpid = corpid;
		this.corpsecret = corpsecret;
	}

	public String requestToken() {
		HttpRequestAdapter request = new HttpRequestAdapter();
		String responseBody = request.get(String.format(TOKEN_URL, corpid, corpsecret));

		AccessToken accessToken = JsonUtils.gsonStringToObject(responseBody, AccessToken.class);
		log.debug("accessToken " + accessToken);
		if (accessToken.getErrcode() == 0) {
			return accessToken.getAccess_token();
		}
		return "";
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getCorpsecret() {
		return corpsecret;
	}

	public void setCorpsecret(String corpsecret) {
		this.corpsecret = corpsecret;
	}

	public static void main(String[] args) {

	}

}
