package com.wy.test.core.entity;

import java.io.Serializable;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dromara.mybatis.jpa.entity.JpaEntity;

@Entity
@Table(name = "MXK_ACCOUNTS_STRATEGY")
public class AccountsStrategy extends JpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8743329570694948718L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	private String id;

	@Column
	private String name;

	@Column
	private String appId;

	private byte[] appIcon;

	private String appIconBase64;

	@Column
	private String appName;

	@Column
	private String mapping;

	@Column
	String filters;

	@Column
	String orgIdsList;

	@Column
	String suffixes;

	@Column
	String createType;

	@Column
	String status;

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
	private String instId;

	private String instName;

	public AccountsStrategy() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getOrgIdsList() {
		return orgIdsList;
	}

	public void setOrgIdsList(String orgIdsList) {
		this.orgIdsList = orgIdsList;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getMapping() {
		return mapping;
	}

	public void setMapping(String mapping) {
		this.mapping = mapping;
	}

	public byte[] getAppIcon() {
		return appIcon;
	}

	public String getAppIconBase64() {
		return appIconBase64;
	}

	public void setAppIconBase64(String appIconBase64) {
		this.appIconBase64 = appIconBase64;
	}

	public void setAppIcon(byte[] appIcon) {
		this.appIcon = appIcon;
	}

	public String getCreateType() {
		return createType;
	}

	public void setCreateType(String createType) {
		this.createType = createType;
	}

	public String getSuffixes() {
		return suffixes;
	}

	public void setSuffixes(String suffixes) {
		this.suffixes = suffixes;
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

	public void transIconBase64() {
		if (this.appIcon != null) {
			this.appIconBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(appIcon);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountsStrategy [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", appId=");
		builder.append(appId);
		builder.append(", appName=");
		builder.append(appName);
		builder.append(", mapping=");
		builder.append(mapping);
		builder.append(", filters=");
		builder.append(filters);
		builder.append(", orgIdsList=");
		builder.append(orgIdsList);
		builder.append(", status=");
		builder.append(status);
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
