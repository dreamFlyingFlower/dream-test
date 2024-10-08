package com.wy.test.protocol.cas.endpoint.adapter;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.vo.AppCasDetailVO;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.protocol.cas.endpoint.response.ServiceResponseBuilder;

import dream.flying.flower.helper.DateTimeHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * CAS默认认证处理器,由Cas20AuthorizeEndpoint调用
 *
 * @author 飞花梦影
 * @date 2024-10-02 16:46:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class CasDefaultAdapter extends AbstractAuthorizeAdapter {

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
		String user = getValueByUserAttr(userInfo, ((AppCasDetailVO) this.app).getCasUser());
		log.debug("cas user {}", user);
		serviceResponseBuilder.success().setUser(user);

		// for user
		serviceResponseBuilder.setAttribute("uid", userInfo.getId());
		serviceResponseBuilder.setAttribute("username", userInfo.getUsername());
		serviceResponseBuilder.setAttribute("displayName", base64Attr(userInfo.getDisplayName()));
		serviceResponseBuilder.setAttribute("firstName", base64Attr(userInfo.getGivenName()));
		serviceResponseBuilder.setAttribute("lastname", base64Attr(userInfo.getFamilyName()));
		serviceResponseBuilder.setAttribute("mobile", userInfo.getMobile());
		serviceResponseBuilder.setAttribute("birthday", DateTimeHelper.formatDate(userInfo.getBirthDate()));
		serviceResponseBuilder.setAttribute("gender", userInfo.getGender() + "");

		// for work
		serviceResponseBuilder.setAttribute("employeeNumber", userInfo.getEmployeeNumber());
		serviceResponseBuilder.setAttribute("title", base64Attr(userInfo.getJobTitle()));
		serviceResponseBuilder.setAttribute("email", userInfo.getWorkEmail());
		serviceResponseBuilder.setAttribute("department", base64Attr(userInfo.getDepartment()));
		serviceResponseBuilder.setAttribute("departmentId", userInfo.getDepartmentId());
		serviceResponseBuilder.setAttribute("workRegion", base64Attr(userInfo.getWorkRegion()));
		serviceResponseBuilder.setAttribute("institution", userInfo.getInstId());
		serviceResponseBuilder.setAttribute(ConstAuthWeb.ONLINE_TICKET_NAME, principal.getSession().getFormattedId());

		return serviceResponseBuilder;
	}

	public void setServiceResponseBuilder(ServiceResponseBuilder serviceResponseBuilder) {
		this.serviceResponseBuilder = serviceResponseBuilder;
	}
}