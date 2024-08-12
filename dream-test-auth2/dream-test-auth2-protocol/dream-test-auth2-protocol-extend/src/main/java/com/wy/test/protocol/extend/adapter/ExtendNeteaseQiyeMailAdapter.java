package com.wy.test.protocol.extend.adapter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.ExtraAttr;
import com.wy.test.core.entity.ExtraAttrs;
import com.wy.test.core.vo.AppVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.protocol.extend.adapter.netease.NeteaseRSATool;

import lombok.extern.slf4j.Slf4j;

/**
 * qiye.163.com
 */
@Slf4j
public class ExtendNeteaseQiyeMailAdapter extends AbstractAuthorizeAdapter {

	// https://entryhz.qiye.163.com
	static String REDIRECT_PARAMETER = "domain=%s&account_name=%s&time=%s&enc=%s&lang=%s";

	static String DEFAULT_REDIRECT_URI = "https://entryhz.qiye.163.com/domain/oa/Entry";

	AccountEntity account;

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		AppVO details = app;
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
		log.debug("Private Key {} ", privateKey);

		String enc = new NeteaseRSATool().generateSHA1withRSASigature(src, privateKey);
		String loginUrl = String.format(redirect_uri.toString(), domain, account_name, time, enc, lang);

		log.debug("LoginUrl {} ", loginUrl);
		modelAndView.addObject("redirect_uri", loginUrl);

		return modelAndView;
	}
}