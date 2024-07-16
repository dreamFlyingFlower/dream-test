package com.wy.test.synchronizer.workweixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.synchronizer.core.synchronizer.entity.AccessToken;
import com.wy.test.util.JsonUtils;
import com.wy.test.web.HttpRequestAdapter;

public class WorkweixinAccessTokenService {

	final static Logger _logger = LoggerFactory.getLogger(WorkweixinAccessTokenService.class);

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
		_logger.debug("accessToken " + accessToken);
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
