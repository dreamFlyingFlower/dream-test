package com.wy.test.sync.weixin;

import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.sync.core.synchronizer.entity.AccessToken;

import dream.flying.flower.framework.core.json.JsonHelpers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeixinAccessTokenService {

	String corpid;

	String corpsecret;

	public static String TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

	public WeixinAccessTokenService() {
	}

	public WeixinAccessTokenService(String corpid, String corpsecret) {
		super();
		this.corpid = corpid;
		this.corpsecret = corpsecret;
	}

	public String requestToken() {
		HttpRequestAdapter request = new HttpRequestAdapter();
		String responseBody = request.get(String.format(TOKEN_URL, corpid, corpsecret));

		AccessToken accessToken = JsonHelpers.read(responseBody, AccessToken.class);
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
