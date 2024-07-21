package com.wy.test.synchronizer.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DingtalkAccessTokenService {

	String appkey;

	String appsecret;

	public DingtalkAccessTokenService() {

	}

	public DingtalkAccessTokenService(String appkey, String appsecret) {
		super();
		this.appkey = appkey;
		this.appsecret = appsecret;
	}

	public String requestToken() throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
		OapiGettokenRequest request = new OapiGettokenRequest();
		request.setAppkey(appkey);
		request.setAppsecret(appsecret);
		request.setHttpMethod("GET");
		OapiGettokenResponse response = client.execute(request);
		log.info("response : " + response.getBody());

		if (response.getErrcode() == 0) {
			return response.getAccessToken();
		}
		return "";
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public static void main(String[] args) {

	}

}
