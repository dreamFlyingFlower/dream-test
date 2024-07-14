package com.wy.test.entity.apps;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MXK_APPS_CAS_DETAILS")
public class AppsCasDetails extends Apps {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4272290765948322084L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	private String id;

	@Column
	private String service;

	@Column
	private Integer expires;

	@Column
	private String callbackUrl;

	@Column
	private String instId;

	@Column
	private String casUser;

	private String instName;

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public Integer getExpires() {
		return expires;
	}

	public void setExpires(Integer expires) {
		this.expires = expires;
	}

	public String getCasUser() {
		return casUser;
	}

	public void setCasUser(String casUser) {
		this.casUser = casUser;
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
		builder.append("AppsCasDetails [id=");
		builder.append(id);
		builder.append(", service=");
		builder.append(service);
		builder.append(", expires=");
		builder.append(expires);
		builder.append(", callbackUrl=");
		builder.append(callbackUrl);
		builder.append(", instId=");
		builder.append(instId);
		builder.append(", instName=");
		builder.append(instName);
		builder.append("]");
		return builder.toString();
	}

}
