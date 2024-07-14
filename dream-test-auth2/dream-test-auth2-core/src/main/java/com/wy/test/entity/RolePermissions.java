package com.wy.test.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wy.test.entity.apps.Apps;

/*
 * ID varchar(40) not null, ROLEID varchar(40) null, MENUID varchar(40) null
 * constraint PK_ROLES primary key clustered (ID)
 */
@Entity
@Table(name = "MXK_ROLE_PERMISSIONS")
public class RolePermissions extends Apps implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8634166407201007340L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@Column
	private String roleId;

	private String roleName;

	@Column
	private String appId;

	private String appName;

	@Column
	private String instId;

	private String instName;

	public RolePermissions() {
		super();
	}

	/**
	 * @param groupId
	 * @param appId
	 */
	public RolePermissions(String roleId, String appId, String instId) {
		super();
		this.roleId = roleId;
		this.appId = appId;
		this.instId = instId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RolePermissions [id=");
		builder.append(id);
		builder.append(", roleId=");
		builder.append(roleId);
		builder.append(", roleName=");
		builder.append(roleName);
		builder.append(", appId=");
		builder.append(appId);
		builder.append(", appName=");
		builder.append(appName);
		builder.append(", instId=");
		builder.append(instId);
		builder.append(", instName=");
		builder.append(instName);
		builder.append("]");
		return builder.toString();
	}

}
