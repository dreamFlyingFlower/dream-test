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
@Table(name = "MXK_EMAIL_SENDERS")
public class EmailSenders extends JpaEntity implements Serializable {

	private static final long serialVersionUID = 3689854324601731505L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	private String id;

	@Column
	private String account;

	@Column
	private String credentials;

	@Column
	private String smtpHost;

	@Column
	private Integer port;

	@Column
	private int sslSwitch;

	@Column
	private String sender;

	@Column
	private String encoding;

	@Column
	private String protocol;

	@Column
	private int status;

	@Column
	private String instId;

	private String instName;

	@Column
	private String description;

	@Column
	private String createdBy;

	@Column
	private String createdDate;

	@Column
	private String modifiedBy;

	@Column
	private String modifiedDate;

	public EmailSenders() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public int getSslSwitch() {
		return sslSwitch;
	}

	public void setSslSwitch(int sslSwitch) {
		this.sslSwitch = sslSwitch;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmailSenders [id=");
		builder.append(id);
		builder.append(", account=");
		builder.append(account);
		builder.append(", credentials=");
		builder.append(credentials);
		builder.append(", smtpHost=");
		builder.append(smtpHost);
		builder.append(", port=");
		builder.append(port);
		builder.append(", sslSwitch=");
		builder.append(sslSwitch);
		builder.append(", sender=");
		builder.append(sender);
		builder.append(", status=");
		builder.append(status);
		builder.append(", instId=");
		builder.append(instId);
		builder.append(", instName=");
		builder.append(instName);
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
		builder.append("]");
		return builder.toString();
	}

}
