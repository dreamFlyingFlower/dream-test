package com.wy.test.synchronizer.feishu;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.constants.ContentType;
import com.wy.test.synchronizer.core.synchronizer.entity.AccessToken;
import com.wy.test.util.JsonUtils;
import com.wy.test.web.HttpRequestAdapter;

public class FeishuAccessTokenService {

	final static Logger _logger = LoggerFactory.getLogger(FeishuAccessTokenService.class);

	String appId;

	String appSecret;

	public static String TOKEN_URL = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";

	public FeishuAccessTokenService() {
	}

	public FeishuAccessTokenService(String appId, String appSecret) {
		super();
		this.appId = appId;
		this.appSecret = appSecret;
	}

	public String requestToken() {
		HttpRequestAdapter request = new HttpRequestAdapter(ContentType.APPLICATION_JSON);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("app_id", appId);
		parameterMap.put("app_secret", appSecret);
		String responseBody = request.post(TOKEN_URL, parameterMap, null);

		AccessToken accessToken = JsonUtils.gsonStringToObject(responseBody, AccessToken.class);
		_logger.debug("accessToken " + accessToken);
		if (accessToken.getErrcode() == 0) {
			return accessToken.getTenant_access_token();
		}
		return "";
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public static void main(String[] args) {

	}

}
