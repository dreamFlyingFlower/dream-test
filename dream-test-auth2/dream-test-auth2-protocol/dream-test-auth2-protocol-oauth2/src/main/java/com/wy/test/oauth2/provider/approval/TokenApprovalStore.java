package com.wy.test.oauth2.provider.approval;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;

import com.wy.test.oauth2.common.OAuth2AccessToken;
import com.wy.test.oauth2.provider.OAuth2Authentication;
import com.wy.test.oauth2.provider.token.TokenStore;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-07-21 23:05:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class TokenApprovalStore implements ApprovalStore {


	private TokenStore store;

	/**
	 * @param store the token store to set
	 */
	public void setTokenStore(TokenStore store) {
		this.store = store;
	}

	/**
	 * This implementation is a no-op. We assume that the {@link TokenStore} is
	 * populated elsewhere, by (for example) a token services instance that knows
	 * more about granted tokens than we could possibly infer from the approvals.
	 * 
	 * @see com.wy.test.oauth2.provider.approval.ApprovalStore#addApprovals(java.util.Collection)
	 */
	@Override
	public boolean addApprovals(Collection<Approval> approvals) {
		return true;
	}

	/**
	 * Revoke all tokens that match the client and user in the approvals supplied.
	 * 
	 * @see com.wy.test.oauth2.provider.approval.ApprovalStore#revokeApprovals(java.util.Collection)
	 */
	@Override
	public boolean revokeApprovals(Collection<Approval> approvals) {
		boolean success = true;
		for (Approval approval : approvals) {
			Collection<OAuth2AccessToken> tokens =
					store.findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId());
			for (OAuth2AccessToken token : tokens) {
				OAuth2Authentication authentication = store.readAuthentication(token);
				if (authentication != null
						&& approval.getClientId().equals(authentication.getOAuth2Request().getClientId())) {
					store.removeAccessToken(token);
				}
			}
		}
		return success;
	}

	/**
	 * Extract the implied approvals from any tokens associated with the user and
	 * client id supplied.
	 * 
	 * @see com.wy.test.oauth2.provider.approval.ApprovalStore#getApprovals(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Collection<Approval> getApprovals(String userId, String clientId) {
		Collection<Approval> result = new HashSet<Approval>();
		Collection<OAuth2AccessToken> tokens = store.findTokensByClientIdAndUserName(clientId, userId);
		for (OAuth2AccessToken token : tokens) {
				OAuth2Authentication authentication = store.readAuthentication(token);
				if (authentication != null) {
					Date expiresAt = token.getExpiration();
					for (String scope : token.getScope()) {
						Approval approval = new Approval(userId, clientId, scope, expiresAt, ApprovalStatus.APPROVED);
						result.add(approval);
					}
				}
		}
		return result;
	}
}