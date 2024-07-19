package com.wy.test.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * ID varchar(40) not null, APPROLEID varchar(40) null, USERID varchar(40) null
 * constraint PK_ROLES primary key clustered (ID)
 */
@Entity
@Table(name = "MXK_ROLE_MEMBER")
public class RoleMember extends UserInfo implements Serializable {

	private static final long serialVersionUID = -8059639972590554760L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "snowflakeid")
	String id;

	@Column
	private String roleId;

	private String roleName;

	private String category;

	@Column
	private String memberId;

	private String memberName;

	@Column
	private String type;// User or Group

	@Column
	private String instId;

	private String instName;

	public RoleMember() {
		super();
	}

	/**
	 * @param groupId
	 * @param memberId
	 * @param type
	 */
	public RoleMember(String roleId, String memberId, String type, String instId) {
		super();
		this.roleId = roleId;
		this.memberId = memberId;
		this.type = type;
		this.instId = instId;
	}

	public RoleMember(String roleId, String roleName, String memberId, String memberName, String type, String instId) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
		this.memberId = memberId;
		this.memberName = memberName;
		this.type = type;
		this.instId = instId;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
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
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
		builder.append("RoleMember [id=");
		builder.append(id);
		builder.append(", roleId=");
		builder.append(roleId);
		builder.append(", roleName=");
		builder.append(roleName);
		builder.append(", category=");
		builder.append(category);
		builder.append(", memberId=");
		builder.append(memberId);
		builder.append(", memberName=");
		builder.append(memberName);
		builder.append(", type=");
		builder.append(type);
		builder.append(", instId=");
		builder.append(instId);
		builder.append(", instName=");
		builder.append(instName);
		builder.append("]");
		return builder.toString();
	}

}