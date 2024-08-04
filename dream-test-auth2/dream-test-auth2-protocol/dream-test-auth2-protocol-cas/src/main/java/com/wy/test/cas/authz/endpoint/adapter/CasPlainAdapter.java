package com.wy.test.cas.authz.endpoint.adapter;

import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.cas.authz.endpoint.response.ServiceResponseBuilder;
import com.wy.test.core.entity.AppCasDetailEntity;
import com.wy.test.core.web.WebConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-07-18 22:13:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class CasPlainAdapter extends AbstractAuthorizeAdapter {

	ServiceResponseBuilder serviceResponseBuilder;

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {

		return modelAndView;
	}

	@Override
	public Object generateInfo() {
		// user for return
		String user = getValueByUserAttr(userInfo, ((AppCasDetailEntity) this.app).getCasUser());
		log.debug("cas user {}", user);
		serviceResponseBuilder.success().setUser(user);

		// for user
		serviceResponseBuilder.setAttribute("uid", userInfo.getId());
		serviceResponseBuilder.setAttribute("username", userInfo.getUsername());
		serviceResponseBuilder.setAttribute("displayName", userInfo.getDisplayName());
		serviceResponseBuilder.setAttribute("firstName", userInfo.getGivenName());
		serviceResponseBuilder.setAttribute("lastname", userInfo.getFamilyName());
		serviceResponseBuilder.setAttribute("mobile", userInfo.getMobile());
		serviceResponseBuilder.setAttribute("birthday", userInfo.getBirthDate());
		serviceResponseBuilder.setAttribute("gender", userInfo.getGender() + "");

		// for work
		serviceResponseBuilder.setAttribute("employeeNumber", userInfo.getEmployeeNumber());
		serviceResponseBuilder.setAttribute("title", userInfo.getJobTitle());
		serviceResponseBuilder.setAttribute("email", userInfo.getWorkEmail());
		serviceResponseBuilder.setAttribute("department", userInfo.getDepartment());
		serviceResponseBuilder.setAttribute("departmentId", userInfo.getDepartmentId());
		serviceResponseBuilder.setAttribute("workRegion", userInfo.getWorkRegion());
		serviceResponseBuilder.setAttribute("institution", userInfo.getInstId());
		serviceResponseBuilder.setAttribute(WebConstants.ONLINE_TICKET_NAME, principal.getSession().getFormattedId());

		return serviceResponseBuilder;
	}

	public void setServiceResponseBuilder(ServiceResponseBuilder serviceResponseBuilder) {
		this.serviceResponseBuilder = serviceResponseBuilder;
	}
}