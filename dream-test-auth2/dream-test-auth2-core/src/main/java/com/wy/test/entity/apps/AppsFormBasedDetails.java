package com.wy.test.entity.apps;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MXK_APPS_FORM_BASED_DETAILS")
public class AppsFormBasedDetails extends Apps {

	/**
	 * 
	 */
	private static final long serialVersionUID = 563313247706861431L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	protected String id;

	@Column
	private String redirectUri;

	@Column
	private String usernameMapping;

	@Column
	private String passwordMapping;

	@Column
	private String passwordAlgorithm;

	@Column
	private String authorizeView;

	@Column
	private String instId;

	private String instName;

	/**
	 * 
	 */
	public AppsFormBasedDetails() {

	}

	/**
	 * @return the redirectUri
	 */
	public String getRedirectUri() {
		return redirectUri;
	}

	/**
	 * @param redirectUri the redirectUri to set
	 */
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	/**
	 * @return the usernameMapping
	 */
	public String getUsernameMapping() {
		return usernameMapping;
	}

	/**
	 * @param usernameMapping the usernameMapping to set
	 */
	public void setUsernameMapping(String usernameMapping) {
		this.usernameMapping = usernameMapping;
	}

	/**
	 * @return the passwordMapping
	 */
	public String getPasswordMapping() {
		return passwordMapping;
	}

	/**
	 * @param passwordMapping the passwordMapping to set
	 */
	public void setPasswordMapping(String passwordMapping) {
		this.passwordMapping = passwordMapping;
	}

	public String getAuthorizeView() {
		return authorizeView;
	}

	public void setAuthorizeView(String authorizeView) {
		this.authorizeView = authorizeView;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getPasswordAlgorithm() {
		return passwordAlgorithm;
	}

	public void setPasswordAlgorithm(String passwordAlgorithm) {
		this.passwordAlgorithm = passwordAlgorithm;
	}

	@Override
	public String getInstId() {
		return instId;
	}

	@Override
	public void setInstId(String instId) {
		this.instId = instId;
	}

	@Override
	public String getInstName() {
		return instName;
	}

	@Override
	public void setInstName(String instName) {
		this.instName = instName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppsFormBasedDetails [id=");
		builder.append(id);
		builder.append(", redirectUri=");
		builder.append(redirectUri);
		builder.append(", usernameMapping=");
		builder.append(usernameMapping);
		builder.append(", passwordMapping=");
		builder.append(passwordMapping);
		builder.append(", passwordAlgorithm=");
		builder.append(passwordAlgorithm);
		builder.append(", authorizeView=");
		builder.append(authorizeView);
		builder.append(", instId=");
		builder.append(instId);
		builder.append(", instName=");
		builder.append(instName);
		builder.append("]");
		return builder.toString();
	}

}
