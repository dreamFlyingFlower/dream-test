package com.wy.test.extend.adapter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.entity.Accounts;
import com.wy.test.entity.ExtraAttr;
import com.wy.test.entity.ExtraAttrs;
import com.wy.test.entity.apps.Apps;
import com.wy.test.extend.adapter.netease.NeteaseRSATool;

/**
 * qiye.163.com
 */
public class ExtendApiNeteaseQiyeMailAdapter extends AbstractAuthorizeAdapter {

	final static Logger _logger = LoggerFactory.getLogger(ExtendApiNeteaseQiyeMailAdapter.class);

	// https://entryhz.qiye.163.com
	static String REDIRECT_PARAMETER = "domain=%s&account_name=%s&time=%s&enc=%s&lang=%s";

	static String DEFAULT_REDIRECT_URI = "https://entryhz.qiye.163.com/domain/oa/Entry";

	Accounts account;

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {

		Apps details = (Apps) app;
		StringBuffer redirect_uri = new StringBuffer(details.getLoginUrl());
		if (StringUtils.isNotBlank(redirect_uri)) {
			if (redirect_uri.indexOf("?") > -1) {
				redirect_uri.append("").append(REDIRECT_PARAMETER);
			} else {
				redirect_uri.append("?").append(REDIRECT_PARAMETER);
			}
		}
		// extraAttrs from App
		ExtraAttrs extraAttrs = null;
		if (details.getIsExtendAttr() == 1) {
			extraAttrs = new ExtraAttrs(details.getExtendAttr());
			for (ExtraAttr attr : extraAttrs.getExtraAttrs()) {
				redirect_uri.append("&").append(attr.getAttr()).append("=").append(attr.getValue());
			}
		}

		String time = System.currentTimeMillis() + "";
		// 域名，请使用企业自己的域名
		String domain = details.getPrincipal();

		String account_name = this.userInfo.getEmail().substring(0, this.userInfo.getEmail().indexOf("@"));

		String lang = "0";
		String src = account_name + domain + time;

		String privateKey = details.getCredentials();
		_logger.debug("Private Key {} ", privateKey);

		String enc = new NeteaseRSATool().generateSHA1withRSASigature(src, privateKey);
		String loginUrl = String.format(redirect_uri.toString(), domain, account_name, time, enc, lang);

		_logger.debug("LoginUrl {} ", loginUrl);
		modelAndView.addObject("redirect_uri", loginUrl);

		return modelAndView;
	}

}
