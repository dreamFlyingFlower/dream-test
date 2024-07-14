package com.wy.test.entity.apps;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MXK_APPS_JWT_DETAILS")
public class AppsJwtDetails extends Apps {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1717427271305620545L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	protected String id;

	@Column
	private String subject;

	@Column
	private String issuer;

	@Column
	private String audience;

	/**
	 * 
	 */
	@Column
	private String redirectUri;

	//
	@Column
	private String tokenType;

	@Column
	private String jwtName;

	@Column
	private String algorithm;

	@Column
	private String algorithmKey;

	@Column
	private String encryptionMethod;

	@Column
	private String signature;

	@Column
	private String signatureKey;

	@Column
	private Integer expires;

	@Column
	private String instId;

	private String instName;

	public AppsJwtDetails() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
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

	public String getJwtName() {
		return jwtName;
	}

	public void setJwtName(String jwtName) {
		this.jwtName = jwtName;
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

	public String getEncryptionMethod() {
		return encryptionMethod;
	}

	public void setEncryptionMethod(String encryptionMethod) {
		this.encryptionMethod = encryptionMethod;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignatureKey() {
		return signatureKey;
	}

	public void setSignatureKey(String signatureKey) {
		this.signatureKey = signatureKey;
	}

	public Integer getExpires() {
		return expires;
	}

	public void setExpires(Integer expires) {
		this.expires = expires;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getInstName() {
		return instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppsJwtDetails [id=");
		builder.append(id);
		builder.append(", redirectUri=");
		builder.append(redirectUri);
		builder.append(", tokenType=");
		builder.append(tokenType);
		builder.append(", jwtName=");
		builder.append(jwtName);
		builder.append(", algorithm=");
		builder.append(algorithm);
		builder.append(", algorithmKey=");
		builder.append(algorithmKey);
		builder.append(", expires=");
		builder.append(expires);
		builder.append(", instId=");
		builder.append(instId);
		builder.append(", instName=");
		builder.append(instName);
		builder.append("]");
		return builder.toString();
	}

}
