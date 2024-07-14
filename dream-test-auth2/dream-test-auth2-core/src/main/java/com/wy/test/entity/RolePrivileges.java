package com.wy.test.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dromara.mybatis.jpa.entity.JpaEntity;

import com.wy.test.constants.ConstsStatus;
import com.wy.test.web.WebContext;

@Entity
@Table(name = "MXK_ROLE_PRIVILEGES")
public class RolePrivileges extends JpaEntity implements Serializable {

	private static final long serialVersionUID = -8783585691243853899L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@Column
	String appId;

	@Column
	String roleId;

	@Column
	String resourceId;

	int status = ConstsStatus.ACTIVE;

	@Column
	private String instId;

	private String instName;

	public RolePrivileges() {
	}

	public RolePrivileges(String appId, String roleId, String instId) {
		this.appId = appId;
		this.roleId = roleId;
		this.instId = instId;
	}

	/**
	 * .
	 * 
	 * @param appId String
	 * @param roleId String
	 * @param resourceId String
	 */
	public RolePrivileges(String appId, String roleId, String resourceId, String instId) {
		this.id = WebContext.genId();
		this.appId = appId;
		this.roleId = roleId;
		this.resourceId = resourceId;
		this.instId = instId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUniqueId() {
		return appId + "_" + roleId + "_" + resourceId;
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
		builder.append("RolePrivileges [id=");
		builder.append(id);
		builder.append(", appId=");
		builder.append(appId);
		builder.append(", roleId=");
		builder.append(roleId);
		builder.append(", resourceId=");
		builder.append(resourceId);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

}
