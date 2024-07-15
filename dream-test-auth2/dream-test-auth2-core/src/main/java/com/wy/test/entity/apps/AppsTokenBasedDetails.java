package com.wy.test.entity.apps;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MXK_APPS_TOKEN_BASED_DETAILS")
public class AppsTokenBasedDetails extends Apps {

	private static final long serialVersionUID = -1717427271305620545L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	protected String id;

	/**
	 * 
	 */
	@Column
	private String redirectUri;

	//
	@Column
	private String tokenType;

	@Column
	private String cookieName;

	@Column
	private String algorithm;

	@Column
	private String algorithmKey;

	@Column
	private Integer expires;

	@Column
	private String instId;

	private String instName;

	public AppsTokenBasedDetails() {
		super();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getAlgorithmKey() {
		return algorithmKey;
	}

	public void setAlgorithmKey(String algorithmKey) {
		this.algorithmKey = algorithmKey;
	}

	public Integer getExpires() {
		return expires;
	}

	public void setExpires(Integer expires) {
		this.expires = expires;
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
		builder.append("AppsTokenBasedDetails [id=");
		builder.append(id);
		builder.append(", redirectUri=");
		builder.append(redirectUri);
		builder.append(", tokenType=");
		builder.append(tokenType);
		builder.append(", cookieName=");
		builder.append(cookieName);
		builder.append(", algorithm=");
		builder.append(algorithm);
		builder.append(", algorithmKey=");
		builder.append(algorithmKey);
		builder.append(", expires=");
		builder.append(expires);
		builder.append("]");
		return builder.toString();
	}

}
