package com.wy.test.authz.oauth2.provider.approval;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.wy.test.authz.oauth2.common.OAuth2AccessToken;
import com.wy.test.authz.oauth2.common.OAuth2Constants;
import com.wy.test.authz.oauth2.provider.AuthorizationRequest;
import com.wy.test.authz.oauth2.provider.ClientDetailsService;
import com.wy.test.authz.oauth2.provider.ClientRegistrationException;
import com.wy.test.authz.oauth2.provider.OAuth2Authentication;
import com.wy.test.authz.oauth2.provider.OAuth2Request;
import com.wy.test.authz.oauth2.provider.OAuth2RequestFactory;
import com.wy.test.authz.oauth2.provider.token.TokenStore;
import com.wy.test.entity.apps.oauth2.provider.ClientDetails;

/**
 * A user approval handler that remembers approval decisions by consulting existing tokens.
 * 
 * @author Dave Syer
 * 
 */
public class TokenStoreUserApprovalHandler implements UserApprovalHandler, InitializingBean {

	private static Log logger = LogFactory.getLog(TokenStoreUserApprovalHandler.class);

	private String approvalParameter = OAuth2Constants.PARAMETER.USER_OAUTH_APPROVAL;
	
	private TokenStore tokenStore;
	
	private ClientDetailsService clientDetailsService;
	
	/**
	 * Service to load client details (optional) for auto approval checks.
	 * 
	 * @param clientDetailsService a client details service
	 */
	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}

	/**
	 * @param approvalParameter the approvalParameter to set
	 */
	public void setApprovalParameter(String approvalParameter) {
		this.approvalParameter = approvalParameter;
	}

	/**
	 * @param tokenStore the token store to set
	 */
	public void setTokenStore(TokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	private OAuth2RequestFactory requestFactory;
	
	public void setRequestFactory(OAuth2RequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}
	
	@Override
	public void afterPropertiesSet() {
		Assert.state(tokenStore != null, "TokenStore must be provided");
		Assert.state(requestFactory != null, "OAuth2RequestFactory must be provided");
	}
	
	/**
	 * Basic implementation just requires the authorization request to be explicitly approved and the user to be
	 * authenticated.
	 * 
	 * @param authorizationRequest The authorization request.
	 * @param userAuthentication the current user authentication
	 * 
	 * @return Whether the specified request has been approved by the current user.
	 */
	@Override
	public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
		return authorizationRequest.isApproved();
	}

	@Override
	public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
		
		boolean approved = false;
		
		String clientId = authorizationRequest.getClientId();
		Set<String> scopes = authorizationRequest.getScope();
		if (clientDetailsService!=null) {
			try {
				ClientDetails client = clientDetailsService.loadClientByClientId(clientId,true);
				approved = true;
				for (String scope : scopes) {
					if (!client.isAutoApprove(scope)) {
						approved = false;
					}
				}
				if (approved) {
					authorizationRequest.setApproved(true);
					return authorizationRequest;
				}
			}
			catch (ClientRegistrationException e) {
				logger.warn("Client registration problem prevent autoapproval check for client=" + clientId);
			}		
		}
		
		OAuth2Request storedOAuth2Request = requestFactory.createOAuth2Request(authorizationRequest);
		
		OAuth2Authentication authentication = new OAuth2Authentication(storedOAuth2Request, userAuthentication);
		if (logger.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder("Looking up existing token for ");
			builder.append("client_id=" + clientId);
			builder.append(", scope=" + scopes);
			builder.append(" and username=" + userAuthentication.getName());
			logger.debug(builder.toString());
		}

		OAuth2AccessToken accessToken = tokenStore.getAccessToken(authentication);
		logger.debug("Existing access token=" + accessToken);
		if (accessToken != null && !accessToken.isExpired()) {
			logger.debug("User already approved with token=" + accessToken);
			// A token was already granted and is still valid, so this is already approved
			approved = true;
		}
		else {
			logger.debug("Checking explicit approval");
			approved = userAuthentication.isAuthenticated() && approved;
		}
		
		authorizationRequest.setApproved(approved);

		return authorizationRequest;
	}

	@Override
	public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest, Authentication userAuthentication) {
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
