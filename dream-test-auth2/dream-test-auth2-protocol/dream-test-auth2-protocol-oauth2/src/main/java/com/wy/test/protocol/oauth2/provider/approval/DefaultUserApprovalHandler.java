package com.wy.test.protocol.oauth2.provider.approval;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

import com.wy.test.protocol.oauth2.provider.AuthorizationRequest;

/**
 * A default user approval handler that doesn't remember any decisions.
 * 
 * @author Dave Syer
 * 
 */
public class DefaultUserApprovalHandler implements UserApprovalHandler {

	private String approvalParameter = OAuth2Utils.USER_OAUTH_APPROVAL;

	/**
	 * @param approvalParameter the approvalParameter to set
	 */
	public void setApprovalParameter(String approvalParameter) {
		this.approvalParameter = approvalParameter;
	}

	/**
	 * Basic implementation just requires the authorization request to be explicitly
	 * approved and the user to be authenticated.
	 * 
	 * @param authorizationRequest The authorization request.
	 * @param userAuthentication the current user authentication
	 * 
	 * @return Whether the specified request has been approved by the current user.
	 */
	@Override
	public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
		if (authorizationRequest.isApproved()) {
			return true;
		}
		return false;
	}

	@Override
	public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
			Authentication userAuthentication) {
		return authorizationRequest;
	}

	@Override
	public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest,
			Authentication userAuthentication) {
		Map<String, String> approvalParameters = authorizationRequest.getApprovalParameters();
		String flag = approvalParameters.get(approvalParameter);
		boolean approved = flag != null && flag.toLowerCase().equals("true");
		authorizationRequest.setApproved(approved);
		return authorizationRequest;
	}

	@Override
	public Map<String, Object> getUserApprovalRequest(AuthorizationRequest authorizationRequest,
			Authentication userAuthentication) {
		Map<String, Object> model = new HashMap<String, Object>();
		// In case of a redirect we might want the request parameters to be included
		model.putAll(authorizationRequest.getRequestParameters());
		return model;
	}

}
