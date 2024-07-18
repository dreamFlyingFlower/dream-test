package com.wy.test.authz.oauth2.provider.approval;

import java.util.Collection;

/**
 * Interface for saving, retrieving and revoking user approvals (per client, per
 * scope).
 * 
 * @author Dave Syer
 *
 */
public interface ApprovalStore {

	public boolean addApprovals(Collection<Approval> approvals);

	public boolean revokeApprovals(Collection<Approval> approvals);

	public Collection<Approval> getApprovals(String userId, String clientId);

}
