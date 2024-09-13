package com.wy.test.protocol.oauth2.provider.userinfo.endpoint;

import java.util.HashMap;

import com.wy.test.core.constant.ConstAuthWeb;
import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.generator.StringGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuthDefaultUserInfoAdapter extends AbstractAuthorizeAdapter {

	ClientDetails clientDetails;

	public OAuthDefaultUserInfoAdapter() {
	}

	public OAuthDefaultUserInfoAdapter(ClientDetails clientDetails) {
		this.clientDetails = clientDetails;
	}

	@Override
	public Object generateInfo() {
		String subject = AbstractAuthorizeAdapter.getValueByUserAttr(userInfo, clientDetails.getSubject());
		log.debug("userId : {} , username : {} , displayName : {} , subject : {}", userInfo.getId(),
				userInfo.getUsername(), userInfo.getDisplayName(), subject);

		HashMap<String, Object> beanMap = new HashMap<String, Object>();
		beanMap.put("randomId", (new StringGenerator()).uuidGenerate());
		beanMap.put("userId", userInfo.getId());
		// for spring security oauth2
		beanMap.put("user", subject);
		beanMap.put("username", subject);

		beanMap.put("displayName", userInfo.getDisplayName());
		beanMap.put("employeeNumber", userInfo.getEmployeeNumber());
		beanMap.put("email", userInfo.getEmail());
		beanMap.put("mobile", userInfo.getMobile());
		beanMap.put("realname", userInfo.getDisplayName());
		beanMap.put("birthday", userInfo.getBirthDate());
		beanMap.put("departmentId", userInfo.getDepartmentId());
		beanMap.put("department", userInfo.getDepartment());
		beanMap.put("createdate", userInfo.getCreateTime());
		beanMap.put("title", userInfo.getJobTitle());
		beanMap.put("state", userInfo.getWorkRegion());
		beanMap.put("gender", userInfo.getGender());
		beanMap.put("institution", userInfo.getInstId());
		beanMap.put(ConstAuthWeb.ONLINE_TICKET_NAME, principal.getSession().getFormattedId());

		String info = JsonHelpers.toString(beanMap);

		return info;
	}

	public ClientDetails getClientDetails() {
		return clientDetails;
	}

	public void setClientDetails(ClientDetails clientDetails) {
		this.clientDetails = clientDetails;
	}
}
