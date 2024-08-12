package com.wy.test.protocol.extend.adapter;

import java.time.Instant;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.ExtraAttrs;
import com.wy.test.core.vo.AppVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.digest.DigestHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * https://www.zentao.net/book/zentaopmshelp/344.html
 * http://www.zentao.net/api.php?m=user&f=apilogin&account=account&code=test&time=timestamp&token=token
 * 
 * $code = 'test'; $key = 'a5246932b0f371263c252384076cd3f0'; $time = '1557034496'; $token = md5($code . $key . $time);
 */
@Slf4j
public class ExtendZentaoAdapter extends AbstractAuthorizeAdapter {

	static String login_url_template = "api.php?m=user&f=apilogin&account=%s&code=%s&time=%s&token=%s";

	static String login_url_m_template = "account=%s&code=%s&time=%s&token=%s";

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
		// extraAttrs from Applications
		ExtraAttrs extraAttrs = null;
		if (details.getIsExtendAttr() == 1) {
			extraAttrs = new ExtraAttrs(details.getExtendAttr());
		}
		log.trace("Extra Attrs " + extraAttrs);
		String code = details.getPrincipal();
		String key = details.getCredentials();
		String time = "" + Instant.now().getEpochSecond();

		String token = DigestHelper.md5Hex(code + key + time);

		log.debug("" + token);
		String account = userInfo.getUsername();

		String redirect_uri = details.getLoginUrl();
		if (redirect_uri.indexOf("api.php?") < 0) {
			if (redirect_uri.endsWith("/")) {
				redirect_uri += String.format(login_url_template, account, code, time, token);
			} else {
				redirect_uri += "/" + String.format(login_url_template, account, code, time, token);
			}
		} else if (redirect_uri.endsWith("&")) {
			redirect_uri += String.format(login_url_m_template, account, code, time, token);
		} else {
			redirect_uri += "&" + String.format(login_url_m_template, account, code, time, token);
		}

		log.debug("redirect_uri : " + redirect_uri);
		modelAndView = new ModelAndView("authorize/redirect_sso_submit");
		modelAndView.addObject("redirect_uri", redirect_uri);

		return modelAndView;
	}
}