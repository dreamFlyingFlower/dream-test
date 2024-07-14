package com.wy.test.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dromara.mybatis.jpa.entity.JpaEntity;

@Entity
@Table(name = "MXK_LDAP_CONTEXT")
public class LdapContext extends JpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4595539647817265938L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@Column
	String product;

	@Column
	String providerUrl;

	@Column
	String principal;

	@Column
	String credentials;

	@Column
	String filters;

	@Column
	String basedn;

	@Column
	String msadDomain;

	@Column
	String accountMapping;

	@Column
	String sslSwitch;

	@Column
	String trustStore;

	@Column
	String trustStorePassword;

	@Column
	String description;

	@Column
	String createdBy;

	@Column
	String createdDate;

	@Column
	String modifiedBy;

	@Column
	String modifiedDate;

	@Column
	int status;

	@Column
	private String instId;

	private String instName;

	public LdapContext() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getBasedn() {
		return basedn;
	}

	public void setBasedn(String basedn) {
		this.basedn = basedn;
	}

	public String getMsadDomain() {
		return msadDomain;
	}

	public void setMsadDomain(String msadDomain) {
		this.msadDomain = msadDomain;
	}

	public String getSslSwitch() {
		return sslSwitch;
	}

	public void setSslSwitch(String sslSwitch) {
		this.sslSwitch = sslSwitch;
	}

	public String getAccountMapping() {
		return accountMapping;
	}

	public void setAccountMapping(String accountMapping) {
		this.accountMapping = accountMapping;
	}

	public String getTrustStore() {
		return trustStore;
	}

	public void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
		builder.append("LdapContext [id=");
		builder.append(id);
		builder.append(", product=");
		builder.append(product);
		builder.append(", providerUrl=");
		builder.append(providerUrl);
		builder.append(", principal=");
		builder.append(principal);
		builder.append(", credentials=");
		builder.append(credentials);
		builder.append(", filters=");
		builder.append(filters);
		builder.append(", basedn=");
		builder.append(basedn);
		builder.append(", msadDomain=");
		builder.append(msadDomain);
		builder.append(", sslSwitch=");
		builder.append(sslSwitch);
		builder.append(", trustStore=");
		builder.append(trustStore);
		builder.append(", trustStorePassword=");
		builder.append(trustStorePassword);
		builder.append(", description=");
		builder.append(description);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append(", status=");
		builder.append(status);
		builder.append(", instId=");
		builder.append(instId);
		builder.append(", instName=");
		builder.append(instName);
		builder.append("]");
		return builder.toString();
	}

}
