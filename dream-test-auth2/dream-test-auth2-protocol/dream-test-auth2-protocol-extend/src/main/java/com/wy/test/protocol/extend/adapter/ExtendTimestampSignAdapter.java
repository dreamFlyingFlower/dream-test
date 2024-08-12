package com.wy.test.protocol.extend.adapter;

import java.time.Instant;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.ExtraAttrs;
import com.wy.test.core.vo.AppVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.digest.enums.MessageDigestType;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * http://target.dream.org/demo/login?code=dream&time=timestamp&token=token
 * login url http://target.dream.org/demo/login?code=%s&timestamp=%s&token=%s
 * 
 * $code = 'dream'; $key = 'a5246932b0f371263c252384076cd3f0'; $timestamp =
 * '1557034496'; $token = md5($code . $key . $time);
 */
@Slf4j
public class ExtendTimestampSignAdapter extends AbstractAuthorizeAdapter {

	AccountEntity account;

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public Object encrypt(Object data, String algorithmKey, String algorithm) {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		AppVO details = app;

		String code = details.getPrincipal();
		String key = details.getCredentials();
		String timestamp = "" + Instant.now().getEpochSecond();
		String token = DigestHelper.md5Hex(code + key + timestamp);

		// extraAttrs from Applications
		ExtraAttrs extraAttrs = null;
		if (details.getIsExtendAttr() == 1) {
			extraAttrs = new ExtraAttrs(details.getExtendAttr());
			if (extraAttrs.get("sign") == null || extraAttrs.get("sign").equalsIgnoreCase("md5")) {
				token = DigestHelper.md5Hex(code + key + timestamp);
			} else if (extraAttrs.get("sign").equalsIgnoreCase("sha")
					|| extraAttrs.get("sign").equalsIgnoreCase("sha1")) {
				token = DigestHelper.digestHex(MessageDigestType.SHA_1, code + key + timestamp);
			} else if (extraAttrs.get("sign").equalsIgnoreCase("sha256")) {
				token = DigestHelper.digestHex(MessageDigestType.SHA_256, code + key + timestamp);
			} else if (extraAttrs.get("sign").equalsIgnoreCase("sha384")) {
				token = DigestHelper.digestHex(MessageDigestType.SHA_384, code + key + timestamp);
			} else if (extraAttrs.get("sign").equalsIgnoreCase("sha512")) {
				token = DigestHelper.digestHex(MessageDigestType.SHA_512, code + key + timestamp);
			}
		}

		log.debug("" + token);
		String account = userInfo.getUsername();

		String redirect_uri = String.format(details.getLoginUrl(), account, code, timestamp, token);

		log.debug("redirect_uri : " + redirect_uri);

		modelAndView.addObject("redirect_uri", redirect_uri);

		return modelAndView;
	}
}