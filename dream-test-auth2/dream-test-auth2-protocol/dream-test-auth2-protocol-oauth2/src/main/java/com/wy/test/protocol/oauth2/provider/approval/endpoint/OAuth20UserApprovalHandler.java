package com.wy.test.protocol.oauth2.provider.approval.endpoint;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.protocol.oauth2.provider.AuthorizationRequest;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;
import com.wy.test.protocol.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;

public class OAuth20UserApprovalHandler extends ApprovalStoreUserApprovalHandler {

	private boolean useApprovalStore = true;

	private ClientDetailsService clientDetailsService;

	/**
	 * Service to load client details (optional) for auto approval checks.
	 * 
	 * @param clientDetailsService a client details service
	 */
	@Override
	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
		super.setClientDetailsService(clientDetailsService);
	}

	/**
	 * @param useApprovalStore the useTokenServices to set
	 */
	public void setUseApprovalStore(boolean useApprovalStore) {
		this.useApprovalStore = useApprovalStore;
	}

	/**
	 * Allows automatic approval for a white list of clients in the implicit grant
	 * case.
	 * 
	 * @param authorizationRequest The authorization request.
	 * @param userAuthentication the current user authentication
	 * 
	 * @return An updated request if it has already been approved by the current
	 *         user.
	 */
	@Override
	public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
			Authentication userAuthentication) {

		boolean approved = false;
		// If we are allowed to check existing approvals this will short circuit the
		// decision
		if (useApprovalStore) {
			authorizationRequest = super.checkForPreApproval(authorizationRequest, userAuthentication);
			approved = authorizationRequest.isApproved();
		} else {
			if (clientDetailsService != null) {
				Collection<String> requestedScopes = authorizationRequest.getScope();
				try {
					ClientDetails client =
							clientDetailsService.loadClientByClientId(authorizationRequest.getClientId(), true);
					for (String scope : requestedScopes) {
						if (client.isAutoApprove(scope) || client.isAutoApprove("all")) {
							approved = true;
							break;
						}
					}
				} catch (ClientRegistrationException e) {
				}
			}
		}
		authorizationRequest.setApproved(approved);

		return authorizationRequest;

	}

}
