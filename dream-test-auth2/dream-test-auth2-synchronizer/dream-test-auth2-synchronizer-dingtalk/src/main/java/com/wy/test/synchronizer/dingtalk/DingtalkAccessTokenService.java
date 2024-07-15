package com.wy.test.synchronizer.dingtalk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;

public class DingtalkAccessTokenService {
	final static Logger _logger = LoggerFactory.getLogger(DingtalkAccessTokenService.class);
	
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
		_logger.info("response : " + response.getBody());
		
		if(response.getErrcode()== 0){
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
