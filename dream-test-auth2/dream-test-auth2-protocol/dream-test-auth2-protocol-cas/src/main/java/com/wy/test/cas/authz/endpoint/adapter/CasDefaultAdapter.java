package com.wy.test.cas.authz.endpoint.adapter;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.cas.authz.endpoint.response.ServiceResponseBuilder;
import com.wy.test.core.entity.AppCasDetailEntity;
import com.wy.test.core.web.WebConstants;

public class CasDefaultAdapter extends AbstractAuthorizeAdapter {

	final static Logger _logger = LoggerFactory.getLogger(CasDefaultAdapter.class);

	static String Charset_UTF8 = "UTF-8";

	ServiceResponseBuilder serviceResponseBuilder;

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {

		return modelAndView;
	}

	public String base64Attr(String attrValue) {
		String b64 = "";
		try {
			b64 = (attrValue == null ? "" : "base64:" + Base64.encodeBase64String(attrValue.getBytes(Charset_UTF8)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return b64;
	}

	@Override
	public Object generateInfo() {
		// user for return
		String user = getValueByUserAttr(userInfo, ((AppCasDetailEntity) this.app).getCasUser());
		_logger.debug("cas user {}", user);
		serviceResponseBuilder.success().setUser(user);

		// for user
		serviceResponseBuilder.setAttribute("uid", userInfo.getId());
		serviceResponseBuilder.setAttribute("username", userInfo.getUsername());
		serviceResponseBuilder.setAttribute("displayName", base64Attr(userInfo.getDisplayName()));
		serviceResponseBuilder.setAttribute("firstName", base64Attr(userInfo.getGivenName()));
		serviceResponseBuilder.setAttribute("lastname", base64Attr(userInfo.getFamilyName()));
		serviceResponseBuilder.setAttribute("mobile", userInfo.getMobile());
		serviceResponseBuilder.setAttribute("birthday", userInfo.getBirthDate());
		serviceResponseBuilder.setAttribute("gender", userInfo.getGender() + "");

		// for work
		serviceResponseBuilder.setAttribute("employeeNumber", userInfo.getEmployeeNumber());
		serviceResponseBuilder.setAttribute("title", base64Attr(userInfo.getJobTitle()));
		serviceResponseBuilder.setAttribute("email", userInfo.getWorkEmail());
		serviceResponseBuilder.setAttribute("department", base64Attr(userInfo.getDepartment()));
		serviceResponseBuilder.setAttribute("departmentId", userInfo.getDepartmentId());
		serviceResponseBuilder.setAttribute("workRegion", base64Attr(userInfo.getWorkRegion()));
		serviceResponseBuilder.setAttribute("institution", userInfo.getInstId());
		serviceResponseBuilder.setAttribute(WebConstants.ONLINE_TICKET_NAME, principal.getSession().getFormattedId());

		return serviceResponseBuilder;
	}

	public void setServiceResponseBuilder(ServiceResponseBuilder serviceResponseBuilder) {
		this.serviceResponseBuilder = serviceResponseBuilder;
	}

}
