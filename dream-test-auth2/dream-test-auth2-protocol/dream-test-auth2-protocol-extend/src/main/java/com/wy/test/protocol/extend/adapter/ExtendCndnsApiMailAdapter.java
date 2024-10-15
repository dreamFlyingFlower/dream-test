package com.wy.test.protocol.extend.adapter;

import java.time.Instant;
import java.util.HashMap;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.ExtraAttrs;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.http.HttpsTrust;
import lombok.extern.slf4j.Slf4j;

/**
 * https://exmail.qq.com/qy_mng_logic/doc exmail sso
 * 
 */
@Slf4j
public class ExtendCndnsApiMailAdapter extends AbstractAuthorizeAdapter {

	// sign no parameter
	// sign=md5(action=getDomainInfo&appid=***&time=1579736456 + md5(token))
	// sign with parameter
	// sign=md5(action=getUserInfo&appid=***&email=admin@dream.org&time=1579736456
	// + md5(token))

	AccountEntity account;

	static String SIGN_STRING = "action=getDomainInfo&appid=%s%s";

	static String SIGN_EMAIL_STRING = "action=getUserInfo&appid=%s&email=%s&time=%s%s";

	static String ADMIN_AUTHKEY_URI =
			"https://www.cndnsapi.com/email/clientmanagement?action=getDomailUrl&appid=%s&sign=%s&time=%s";

	static String AUTHKEY_URI =
			"https://www.cndnsapi.com/email/clientmanagement?action=getWebMailUrl&appid=%s&sign=%s&time=%s";

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public Object encrypt(Object data, String algorithmKey, String algorithm) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		HttpsTrust.trustAllCerts();
		AppVO details = app;
		// extraAttrs from Applications
		ExtraAttrs extraAttrs = null;
		String action = "getWebMailUrl";
		String domain = null;
		if (details.getIsExtendAttr() == 1) {
			extraAttrs = new ExtraAttrs(details.getExtendAttr());
			if (extraAttrs.get("action") == null || extraAttrs.get("action").equalsIgnoreCase("getWebMailUrl")) {
				action = "getWebMailUrl";
			} else if (extraAttrs.get("action").equalsIgnoreCase("getDomailUrl")) {
				action = "getDomailUrl";
				domain = extraAttrs.get("domain");
			}
		}

		String timestamp = "" + Instant.now().getEpochSecond();

		String tokenMd5 = DigestHelper.md5Hex(details.getCredentials());
		HashMap<String, Object> requestParamenter = new HashMap<String, Object>();
		String redirect_uri = "";
		if (action.equalsIgnoreCase("getDomailUrl")) {
			String sign = DigestHelper.md5Hex(String.format(SIGN_STRING, details.getPrincipal(), timestamp, tokenMd5));
			requestParamenter.put("domain", domain);
			String responseBody = new HttpRequestAdapter()
					.post(String.format(ADMIN_AUTHKEY_URI, details.getPrincipal(), sign, timestamp), requestParamenter);

			HashMap<String, String> authKey = JsonHelpers.read(responseBody, HashMap.class);
			redirect_uri = authKey.get("adminUrl");

		} else {
			String sign = DigestHelper.md5Hex(
					String.format(SIGN_EMAIL_STRING, details.getPrincipal(), userInfo.getEmail(), timestamp, tokenMd5));
			requestParamenter.put("email", userInfo.getWorkEmail());
			String responseBody = new HttpRequestAdapter()
					.post(String.format(AUTHKEY_URI, details.getPrincipal(), sign, timestamp), requestParamenter);

			HashMap<String, String> authKey = JsonHelpers.read(responseBody, HashMap.class);
			redirect_uri = authKey.get("webmailUrl");
		}

		log.debug("redirect_uri : " + redirect_uri);

		modelAndView.addObject("redirect_uri", redirect_uri);

		return modelAndView;
	}
}