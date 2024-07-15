package org.maxkey.authz.formbased.endpoint.adapter;

import java.util.Date;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.crypto.DigestUtils;
import com.wy.test.entity.apps.AppsFormBasedDetails;

public class FormBasedNeteaseNoteYoudaoAdapter extends AbstractAuthorizeAdapter {

	@Override
	public Object generateInfo() {
		return null;
	}


	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		modelAndView.setViewName("authorize/formbased_wy_youdao_sso_submint");
		AppsFormBasedDetails details=(AppsFormBasedDetails)app;
		modelAndView.addObject("username", account.getRelatedUsername());
		modelAndView.addObject("password",  DigestUtils.md5Hex(account.getRelatedPassword()));
		modelAndView.addObject("currentTime",  (new Date()).getTime());
		return modelAndView;
	}

}
